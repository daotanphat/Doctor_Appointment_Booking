package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.request.UpdateUserRequest;
import com.dtp.doctor_appointment_booking.dto.response.UserResponse;
import com.dtp.doctor_appointment_booking.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User getUserInfo(String email);

    UserResponse updateUserInfo(UpdateUserRequest request, MultipartFile image);
}
