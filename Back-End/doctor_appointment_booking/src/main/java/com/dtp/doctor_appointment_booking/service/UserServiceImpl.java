package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.request.UpdateUserRequest;
import com.dtp.doctor_appointment_booking.dto.response.UserResponse;
import com.dtp.doctor_appointment_booking.exception.EntityNotFoundException;
import com.dtp.doctor_appointment_booking.mapper.UserMapper;
import com.dtp.doctor_appointment_booking.model.User;
import com.dtp.doctor_appointment_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    public UserResponse getUserInfo(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return UserMapper.INSTANCE.entityToResponse(userOptional.get());
        }
        throw new EntityNotFoundException(User.class);
    }

    @Override
    public UserResponse updateUserInfo(UpdateUserRequest request, MultipartFile image) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException(User.class);
        }

        User user = userOptional.get();

        if (request.getFullName() != null && !request.getFullName().equals(user.getFullName())) {
            user.setFullName(request.getFullName());
        }

        if (request.getDateOfBirth() != null && !request.getDateOfBirth().equals(user.getDateOfBirth())) {
            user.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            user.setPhone(request.getPhone());
        }

        if (request.getAddress() != null && !request.getAddress().equals(user.getAddress())) {
            user.setAddress(request.getAddress());
        }

        if (request.isGender() != user.isGender()) {
            user.setGender(request.isGender());
        }

        if (image != null && !image.isEmpty()) {
            try {
                String imageUrl = cloudinaryService
                        .uploadFile(image, "doctor_appointment_booking/users")
                        .get("secure_url")
                        .toString();
                user.setImageUrl(imageUrl);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
        userRepository.save(user);
        return UserMapper.INSTANCE.entityToResponse(user);
    }
}
