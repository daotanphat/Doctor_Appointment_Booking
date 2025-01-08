package com.dtp.doctor_appointment_booking.controller;

import com.dtp.doctor_appointment_booking.dto.DoctorBusy.request.CreateDoctorBusyRequest;
import com.dtp.doctor_appointment_booking.dto.response.MessageResponse;
import com.dtp.doctor_appointment_booking.service.DoctorBusyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor-busy")
@Validated
public class DoctorBusyController {
    private final DoctorBusyService doctorBusyService;

    public DoctorBusyController(DoctorBusyService doctorBusyService) {
        this.doctorBusyService = doctorBusyService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDoctorBusy(@Valid @RequestBody CreateDoctorBusyRequest request) {
        doctorBusyService.saveDoctorBusy(request);
        MessageResponse messageResponse = new MessageResponse("Save busy time successfully");
        return ResponseEntity.ok().body(messageResponse);
    }
}
