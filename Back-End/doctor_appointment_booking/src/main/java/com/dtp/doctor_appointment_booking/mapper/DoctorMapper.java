package com.dtp.doctor_appointment_booking.mapper;

import com.dtp.doctor_appointment_booking.dto.request.AddDoctorRequest;
import com.dtp.doctor_appointment_booking.model.Doctor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(target = "fullName", source = "name")
    @Mapping(target = "fee", source = "fees")
    @Mapping(target = "speciality.id", source = "speciality")
    @Mapping(target = "degree", source = "education")
    @Mapping(target = "description", source = "about")
    @Mapping(target = "status", constant = "true")
    Doctor addRequestToEntity(AddDoctorRequest request);
}
