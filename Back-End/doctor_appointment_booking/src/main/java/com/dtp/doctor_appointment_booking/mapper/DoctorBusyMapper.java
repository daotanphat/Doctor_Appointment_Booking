package com.dtp.doctor_appointment_booking.mapper;

import com.dtp.doctor_appointment_booking.dto.DoctorBusy.response.DoctorBusyResponse;
import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DoctorBusyMapper {
    DoctorBusyMapper INSTANCE = Mappers.getMapper(DoctorBusyMapper.class);

    @Mapping(target = "doctor", source = "doctor.fullName")
    @Mapping(target = "timeSlotFrom", source = "timeSlotFrom.time")
    @Mapping(target = "timeSlotTo", source = "timeSlotTo.time")
    @Mapping(target = "createBy", source = "createBy.fullName")
    DoctorBusyResponse entityToResponse(DoctorBusy doctorBusy);

    List<DoctorBusyResponse> entitiesToResponse(List<DoctorBusy> doctorBusies);
}
