package com.dtp.doctor_appointment_booking.mapper;

import com.dtp.doctor_appointment_booking.dto.request.AddSpecialityRequest;
import com.dtp.doctor_appointment_booking.model.Speciality;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SpecialityMapper {
    SpecialityMapper INSTANCE = Mappers.getMapper(SpecialityMapper.class);

    @Mapping(target = "id", ignore = true)
    Speciality addRequestToEntity(AddSpecialityRequest request);
}
