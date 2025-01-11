package com.dtp.doctor_appointment_booking.controller;

import com.dtp.doctor_appointment_booking.dto.appointment.AppointmentResponse;
import com.dtp.doctor_appointment_booking.dto.appointment.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.dto.payment.PaymentResponse;
import com.dtp.doctor_appointment_booking.dto.payment.PaymentStatusResponse;
import com.dtp.doctor_appointment_booking.dto.response.MessageResponse;
import com.dtp.doctor_appointment_booking.dto.response.PageResponse;
import com.dtp.doctor_appointment_booking.mapper.AppointmentMapper;
import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.AppointmentStatus;
import com.dtp.doctor_appointment_booking.model.Role;
import com.dtp.doctor_appointment_booking.model.User;
import com.dtp.doctor_appointment_booking.repository.AppointmentRepository;
import com.dtp.doctor_appointment_booking.repository.AppointmentStatusRepository;
import com.dtp.doctor_appointment_booking.security.jwt.JwtUtils;
import com.dtp.doctor_appointment_booking.service.AppointmentService;
import com.dtp.doctor_appointment_booking.service.UserService;
import com.dtp.doctor_appointment_booking.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final JwtUtils jwtUtils;
    private final AppointmentStatusRepository appointmentStatusRepository;
    private final AppointmentMapper appointmentMapper;
    private final VnPayService vnPayService;
    private final UserService userService;

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

    @GetMapping("/payment/{appointmentId}")
    public ResponseEntity<?> payAppointment(@PathVariable String appointmentId,
                                            HttpServletRequest request) throws UnsupportedEncodingException {
        PaymentResponse paymentResponse = vnPayService.createPayment(appointmentId, request);
        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping("/payment_info")
    public ResponseEntity<?> updatePaymentStatus(HttpServletRequest request) throws Exception {
        PaymentStatusResponse paymentStatusResponse = vnPayService.vnPayResponse(request);
        return ResponseEntity.ok(paymentStatusResponse);
    }

    @GetMapping("/doctor-appointments")
    public ResponseEntity<PageResponse<AppointmentResponse>> getAppointmentsByDoctor(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "patientEmail", required = false) String patientEmail,
            @RequestParam(value = "dateSlot", required = false) LocalDate dateSlot,
            @RequestParam(value = "paymentStatus", required = false) String paymentStatus,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "dateDesc", defaultValue = "true") boolean dateDesc,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        token = token.replace("Bearer ", "");
        String doctorEmail = jwtUtils.getUserNameFromJwtToken(token);
        Page<Appointment> appointmentsPage = appointmentService.getAppointmentByDoctor(doctorEmail, patientEmail,
                dateSlot, paymentStatus, status, dateDesc, page, size);
        List<Appointment> appointments = appointmentsPage.getContent();
        int totalPages = appointmentsPage.getTotalPages();

        List<AppointmentResponse> appointmentResponses = appointmentMapper.entitiesToResponses(appointments);
        PageResponse<AppointmentResponse> response = new PageResponse<>(appointmentResponses, totalPages);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{status}")
    public ResponseEntity<Integer> getNumberAppointmentByStatus(@RequestHeader("Authorization") String token,
                                                                @PathVariable String status) {
        token = token.replace("Bearer ", "");
        String email = jwtUtils.getUserNameFromJwtToken(token);

        User user = userService.getUserInfo(email);
        int num = appointmentService.numberAppointmentByStatus(user, status);

        return ResponseEntity.ok(num);
    }
}
