package com.dtp.doctor_appointment_booking.controller;

import com.dtp.doctor_appointment_booking.dto.appointment.AppointmentResponse;
import com.dtp.doctor_appointment_booking.dto.appointment.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.dto.response.MessageResponse;
import com.dtp.doctor_appointment_booking.dto.response.PageResponse;
import com.dtp.doctor_appointment_booking.mapper.AppointmentMapper;
import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.AppointmentStatus;
import com.dtp.doctor_appointment_booking.repository.AppointmentRepository;
import com.dtp.doctor_appointment_booking.repository.AppointmentStatusRepository;
import com.dtp.doctor_appointment_booking.security.jwt.JwtUtils;
import com.dtp.doctor_appointment_booking.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final JwtUtils jwtUtils;
    private final AppointmentStatusRepository appointmentStatusRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(AppointmentService appointmentService, JwtUtils jwtUtils,
                                 AppointmentStatusRepository appointmentStatusRepository, AppointmentRepository appointmentRepository,
                                 AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.jwtUtils = jwtUtils;
        this.appointmentStatusRepository = appointmentStatusRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@Valid @RequestBody BookAppointmentRequest request) {
        appointmentService.bookAppointment(request);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Appointment booked successfully");
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<PageResponse<AppointmentResponse>> getMyAppointments(@RequestHeader("Authorization") String token,
                                                                               @RequestParam(value = "search", required = false) String search,
                                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        token = token.replace("Bearer ", "");
        String email = jwtUtils.getUserNameFromJwtToken(token);

        Page<Appointment> appointmentsPage = appointmentService.getMyAppointments(email, search, page, size);
        List<Appointment> appointments = new ArrayList<>(appointmentsPage.getContent());
//        List<AppointmentResponse> appointmentResponses = AppointmentMapper.INSTANCE.entitiesToResponses(appointments);
        List<AppointmentResponse> appointmentResponses = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentResponse response = new AppointmentResponse();
            response.setAppointment_id(appointment.getAppointment_id());
            response.setDoctor(appointment.getDoctor().getFullName());
            response.setPatient(appointment.getPatient().getFullName());
            response.setFee(appointment.getFee());
            response.setDateSlot(appointment.getDateSlot());
            response.setTimeSlotFrom(appointment.getTimeSlotFrom().getTime());
            response.setTimeSlotTo(appointment.getTimeSlotTo().getTime());
            response.setAddress(appointment.getAddress());

            AppointmentStatus appointmentStatus = appointmentStatusRepository
                    .findByAppointmentIdOrderByCreatedAtDesc(appointment.getAppointment_id());
            response.setStatus(appointmentStatus.getStatus().getStatus());

            response.setPaymentStatus(appointment.getPaymentStatus());
            response.setCreatedAt(appointment.getCreatedAt());
            appointmentResponses.add(response);
        }
        PageResponse<AppointmentResponse> response = new PageResponse<>(appointmentResponses, appointmentsPage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{appointmentId}")
    public ResponseEntity<MessageResponse> cancelAppointment(@PathVariable("appointmentId") String appointmentId) {
        Appointment appointment = appointmentService.cancelAppointment(appointmentId);
        if (appointment == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("There is error when cancelling appointment. Please try again later!"));
        }
        return ResponseEntity.ok(new MessageResponse("Appointment cancelled successfully"));
    }

    @GetMapping("/{appointmentId}/details")
    public ResponseEntity<AppointmentResponse> getAppointmentDetails(@PathVariable("appointmentId") String appointmentId) {
        Appointment appointment = appointmentService.getAppointment(appointmentId);
        AppointmentResponse response = appointmentMapper.entityToResponse(appointment);
        return ResponseEntity.ok(response);
    }
}
