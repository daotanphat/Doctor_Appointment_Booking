package com.dtp.doctor_appointment_booking.controller;

import com.dtp.doctor_appointment_booking.dto.DoctorBusy.request.CreateDoctorBusyRequest;
import com.dtp.doctor_appointment_booking.dto.DoctorBusy.request.GetBusyTimeRequest;
import com.dtp.doctor_appointment_booking.dto.DoctorBusy.response.DoctorBusyResponse;
import com.dtp.doctor_appointment_booking.dto.response.MessageResponse;
import com.dtp.doctor_appointment_booking.mapper.DoctorBusyMapper;
import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import com.dtp.doctor_appointment_booking.model.User;
import com.dtp.doctor_appointment_booking.security.jwt.JwtUtils;
import com.dtp.doctor_appointment_booking.service.DoctorBusyService;
import com.dtp.doctor_appointment_booking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor-busy")
@Validated
public class DoctorBusyController {
    private final DoctorBusyService doctorBusyService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public DoctorBusyController(DoctorBusyService doctorBusyService, JwtUtils jwtUtils,
                                UserService userService) {
        this.doctorBusyService = doctorBusyService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDoctorBusy(@Valid @RequestBody CreateDoctorBusyRequest request) {
        doctorBusyService.saveDoctorBusy(request);
        MessageResponse messageResponse = new MessageResponse("Save busy time successfully");
        return ResponseEntity.ok().body(messageResponse);
    }

    @GetMapping("/created-by-doctor/{date}")
    public ResponseEntity<List<DoctorBusyResponse>> getBusyTimeDoctorCreatedAndDate(
            @RequestHeader("Authorization") String token,
            @PathVariable LocalDate date) {
        token = token.replace("Bearer ", "");
        String email = jwtUtils.getUserNameFromJwtToken(token);

        User user = userService.getUserInfo(email);

        List<DoctorBusy> doctorBusies = doctorBusyService
                .getTimeBusyCreateByDoctorAndDate(user.getId(), date);

        return ResponseEntity
                .ok(DoctorBusyMapper.INSTANCE.entitiesToResponse(doctorBusies));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteDoctorBusy(@PathVariable int id) {
        doctorBusyService.deleteDoctorBusy(id);

        MessageResponse messageResponse = new MessageResponse("Remove busy time successfully");

        return ResponseEntity.ok(messageResponse);
    }
}
