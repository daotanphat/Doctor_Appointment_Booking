package com.dtp.doctor_appointment_booking.controller;

import com.dtp.doctor_appointment_booking.dto.request.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.dto.response.MessageResponse;
import com.dtp.doctor_appointment_booking.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@Valid @RequestBody BookAppointmentRequest request) {
        appointmentService.bookAppointment(request);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Appointment booked successfully");
        return ResponseEntity.ok(messageResponse);
    }
}
