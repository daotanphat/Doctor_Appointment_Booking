package com.dtp.doctor_appointment_booking.mapper;

import com.dtp.doctor_appointment_booking.dto.request.UpdateUserRequest;
import com.dtp.doctor_appointment_booking.dto.response.UserResponse;
import com.dtp.doctor_appointment_booking.model.Doctor;
import com.dtp.doctor_appointment_booking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse entityToResponse(User user);

    @Mapping(target = "email", ignore = true)
    User updateRequestToEntity(UpdateUserRequest request);
}
