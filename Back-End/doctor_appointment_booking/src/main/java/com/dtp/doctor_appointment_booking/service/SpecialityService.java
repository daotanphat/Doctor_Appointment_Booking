package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.model.Speciality;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SpecialityService {
    Speciality saveSpeciality(String name, MultipartFile image);

    List<Speciality> getAllSpecialities();
}
