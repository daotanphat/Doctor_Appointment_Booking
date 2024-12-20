package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.request.AddDoctorRequest;
import com.dtp.doctor_appointment_booking.model.Doctor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface DoctorService {
    Doctor saveDoctor(AddDoctorRequest request, MultipartFile image);
}
