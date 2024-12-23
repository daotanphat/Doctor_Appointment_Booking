package com.dtp.doctor_appointment_booking.controller;

import com.dtp.doctor_appointment_booking.dto.response.DoctorResponse;
import com.dtp.doctor_appointment_booking.dto.response.PageResponse;
import com.dtp.doctor_appointment_booking.model.Speciality;
import com.dtp.doctor_appointment_booking.service.DoctorService;
import com.dtp.doctor_appointment_booking.service.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    DoctorService doctorService;

    @Autowired
    SpecialityService specialityService;

    @GetMapping("/doctor-list")
    public ResponseEntity<?> getDoctorsAvailable(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "speciality", required = false) String speciality,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size) {

        Page<DoctorResponse> doctorsPage = doctorService.getDoctorsAvailable(search, speciality, page, size);
        List<DoctorResponse> doctors = doctorsPage.getContent();

        PageResponse<DoctorResponse> response = new PageResponse<>(doctors, doctorsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/specialities")
    public ResponseEntity<List<Speciality>> getSpecialities() {
        return ResponseEntity.ok(specialityService.getAllSpecialities());
    }

    @GetMapping("doctor/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable String id) {
        DoctorResponse doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/doctor/speciality/{speciality}")
    public ResponseEntity<List<DoctorResponse>> getDoctorBySpeciality(@PathVariable String speciality) {
        List<DoctorResponse> doctors = doctorService.getDoctorBySpeciality(speciality);
        return ResponseEntity.ok(doctors);
    }
}
