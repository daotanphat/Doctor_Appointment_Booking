package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.request.UpdateUserRequest;
import com.dtp.doctor_appointment_booking.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResponse getUserInfo(String email);

    UserResponse updateUserInfo(UpdateUserRequest request, MultipartFile image);
}
