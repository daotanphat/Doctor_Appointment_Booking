package com.dtp.doctor_appointment_booking.mapper;

import com.dtp.doctor_appointment_booking.dto.request.AddDoctorRequest;
import com.dtp.doctor_appointment_booking.dto.response.DoctorResponse;
import com.dtp.doctor_appointment_booking.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

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

    @Mapping(target = "doctorId", source = "doctor_id")
    @Mapping(target = "name", source = "fullName")
    @Mapping(target = "speciality", source = "speciality.name")
    DoctorResponse entityToResponse(Doctor doctor);
}
