package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.request.AddDoctorRequest;
import com.dtp.doctor_appointment_booking.dto.response.DoctorResponse;
import com.dtp.doctor_appointment_booking.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor saveDoctor(AddDoctorRequest request, MultipartFile image);

    Page<DoctorResponse> getDoctorsAvailable(String search, String speciality, int page, int size);

    DoctorResponse getDoctorById(String id);

    List<DoctorResponse> getDoctorBySpeciality(String speciality);
}
