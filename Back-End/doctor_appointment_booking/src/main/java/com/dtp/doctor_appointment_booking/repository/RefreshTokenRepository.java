package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.RefreshToken;
import com.dtp.doctor_appointment_booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken findByUserId(int userId);

    @Modifying
    int deleteByUser(User user);
}
