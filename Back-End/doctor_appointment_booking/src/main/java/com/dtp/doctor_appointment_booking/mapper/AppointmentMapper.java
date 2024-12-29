package com.dtp.doctor_appointment_booking.mapper;

import com.dtp.doctor_appointment_booking.dto.request.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(target = "doctor.doctor_id", source = "doctor_id")
    @Mapping(target = "patient.email", source = "patient_mail")
    @Mapping(target = "timeSlotFrom.id", source = "timeSlotFrom")
    @Mapping(target = "timeSlotTo.id", source = "timeSlotTo")
    Appointment bookRequestToEntity(BookAppointmentRequest request);
}
