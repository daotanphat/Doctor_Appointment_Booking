package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    Status findByStatus(String status);
}
