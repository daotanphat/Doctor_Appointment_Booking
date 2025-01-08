package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.request.AddDoctorRequest;
import com.dtp.doctor_appointment_booking.dto.response.DoctorResponse;
import com.dtp.doctor_appointment_booking.exception.EntityNotFoundException;
import com.dtp.doctor_appointment_booking.exception.IllegalArgumentException;
import com.dtp.doctor_appointment_booking.mapper.DoctorMapper;
import com.dtp.doctor_appointment_booking.model.Doctor;
import com.dtp.doctor_appointment_booking.model.Role;
import com.dtp.doctor_appointment_booking.model.User;
import com.dtp.doctor_appointment_booking.repository.DoctorRepository;
import com.dtp.doctor_appointment_booking.repository.RoleRepository;
import com.dtp.doctor_appointment_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, CloudinaryService cloudinaryService,
                             PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.doctorRepository = doctorRepository;
        this.cloudinaryService = cloudinaryService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Doctor saveDoctor(AddDoctorRequest request, MultipartFile image) {
        Optional<Doctor> doctorExist = doctorRepository.findByEmail(request.getEmail());
        if (doctorExist.isPresent()) {
            throw new IllegalArgumentException("Doctor with email " + request.getEmail() + " already exists");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password

        // Map add doctor request to doctor entity
        Doctor doctor = DoctorMapper.INSTANCE.addRequestToEntity(request);

        try {
            String imageUrl = cloudinaryService
                    .uploadFile(image, "doctor_appointment_booking/doctors")
                    .get("secure_url")
                    .toString();
            doctor.setImageUrl(imageUrl); // Upload image to cloudinary and set image url to doctor entity
            Doctor doctorSaved = doctorRepository.save(doctor);

            // save user to database
            User user = new User();
            user.setEmail(doctor.getEmail());
            user.setPassword(doctor.getPassword());
            user.setFullName(doctor.getFullName());
            user.setDateOfBirth(doctor.getDateOfBirth());
            user.setPhone(doctor.getPhone());
            user.setAddress(doctor.getAddress());
            user.setGender(doctor.isGender());
            user.setImageUrl(doctor.getImageUrl());
            user.setStatus(doctor.isStatus());

            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName("DOCTOR")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(role);
            user.setRoles(roles);

            userRepository.save(user);

            return doctorSaved;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public DoctorResponse getDoctorById(String id) {
        Optional<Doctor> doctor = doctorRepository.findByDoctor_id(id);
        if (doctor.isPresent()) {
            DoctorResponse response = DoctorMapper.INSTANCE.entityToResponse(doctor.get());
            return response;
        }
        throw new EntityNotFoundException(Doctor.class);
    }

    @Override
    public Page<DoctorResponse> getDoctorsAvailable(String search, String speciality, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorResponse> doctors = doctorRepository.getDoctorByPageAndFilter(search, speciality, pageable);
        return doctors;
    }

    @Override
    public List<DoctorResponse> getDoctorBySpeciality(String speciality) {
        return doctorRepository.getDoctorBySpecialityName(speciality);
    }

    @Override
    @Transactional
    public Doctor updateDoctorStatus(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctor_id(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(Doctor.class));
        doctor.setStatus(!doctor.isStatus());
        doctorRepository.save(doctor);
        return doctor;
    }

    @Override
    public Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(Doctor.class));
    }
}