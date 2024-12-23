package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.exception.EntityNotFoundException;
import com.dtp.doctor_appointment_booking.exception.IllegalArgumentException;
import com.dtp.doctor_appointment_booking.model.Speciality;
import com.dtp.doctor_appointment_booking.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SpecialityServiceImpl implements SpecialityService {

    @Autowired
    SpecialityRepository specialityRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @Override
    public Speciality saveSpeciality(String name, MultipartFile image) {
        Optional<Speciality> specialityExist = specialityRepository.findByName(name);
        if (specialityExist.isPresent()) {
            throw new IllegalArgumentException("Speciality " + name + " already exists");
        }
        Speciality speciality = null;
        try {
            String imageUrl = cloudinaryService
                    .uploadFile(image, "doctor_appointment_booking/specialities")
                    .get("secure_url")
                    .toString();

            speciality = new Speciality();
            speciality.setName(name);
            speciality.setImageUrl(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return specialityRepository.save(speciality);
    }

    @Override
    public List<Speciality> getAllSpecialities() {
        return specialityRepository.findAll();
    }
}
