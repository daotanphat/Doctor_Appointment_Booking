package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
    Optional<TimeSlot> findByTime(LocalTime time);
}
