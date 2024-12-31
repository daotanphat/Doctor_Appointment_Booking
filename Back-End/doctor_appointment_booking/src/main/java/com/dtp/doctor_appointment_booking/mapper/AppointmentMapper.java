package com.dtp.doctor_appointment_booking.mapper;

import com.dtp.doctor_appointment_booking.dto.appointment.AppointmentResponse;
import com.dtp.doctor_appointment_booking.dto.appointment.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.AppointmentStatus;
import com.dtp.doctor_appointment_booking.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.List;

@Mapper
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "timeSlotFrom.id", source = "timeSlotFrom")
    @Mapping(target = "timeSlotTo.id", source = "timeSlotTo")
    Appointment bookRequestToEntity(BookAppointmentRequest request);

    @Mapping(target = "doctor", source = "doctor.fullName")
    @Mapping(target = "patient", source = "patient.fullName")
    @Mapping(target = "timeSlotFrom", source = "timeSlotFrom.time")
    @Mapping(target = "timeSlotTo", source = "timeSlotTo.time")
    @Mapping(target = "status", expression = "java(mapStatus(appointment))")
    AppointmentResponse entityToResponse(Appointment appointment);

    List<AppointmentResponse> entitiesToResponses(List<Appointment> appointments);

    default String mapStatus(Appointment appointment) {
        return appointment.getStatuses().stream()
                .max(Comparator.comparing(AppointmentStatus::getCreatedAt))
                .map(AppointmentStatus::getStatus)
                .map(Status::getStatus)
                .orElse(null);
    }
}
