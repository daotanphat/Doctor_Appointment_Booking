package com.dtp.doctor_appointment_booking.mapper;

import com.dtp.doctor_appointment_booking.dto.appointment.AppointmentResponse;
import com.dtp.doctor_appointment_booking.dto.appointment.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.AppointmentStatus;
import com.dtp.doctor_appointment_booking.model.Status;
import com.dtp.doctor_appointment_booking.repository.AppointmentStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AppointmentMapper {
    @Autowired
    AppointmentStatusRepository appointmentStatusRepository;

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "timeSlotFrom.id", source = "timeSlotFrom")
    @Mapping(target = "timeSlotTo.id", source = "timeSlotTo")
    public abstract Appointment bookRequestToEntity(BookAppointmentRequest request);

    public AppointmentResponse entityToResponse(Appointment appointment) {
        AppointmentStatus appointmentStatus = appointmentStatusRepository
                .findByAppointmentIdOrderByCreatedAtDesc(appointment.getAppointment_id());
        return new AppointmentResponse(
                appointment.getAppointment_id(),
                appointment.getDoctor().getFullName(),
                appointment.getPatient().getFullName(),
                appointment.getFee(),
                appointment.getDateSlot(),
                appointment.getTimeSlotFrom().getTime(),
                appointment.getTimeSlotTo().getTime(),
                appointment.getAddress(),
                appointmentStatus.getStatus().getStatus(),
                appointment.getPaymentStatus(),
                appointment.getCreatedAt()
        );
    }

    public List<AppointmentResponse> entitiesToResponses(List<Appointment> appointments) {
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

        return appointmentResponses;
    }

    public String mapStatus(Appointment appointment) {
        List<AppointmentStatus> statuses = new ArrayList<>(appointment.getStatuses());
        return statuses.stream()
                .max(Comparator.comparing(AppointmentStatus::getCreatedAt))
                .map(AppointmentStatus::getStatus)
                .map(Status::getStatus)
                .orElse(null);
    }
}
