package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {
    @Query("SELECT a FROM AppointmentStatus a" +
            " WHERE a.appointment.appointment_id = :appointmentId" +
            " ORDER BY a.createdAt DESC" +
            " LIMIT 1")
    AppointmentStatus findByAppointmentIdOrderByCreatedAtDesc(String appointmentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AppointmentStatus a" +
            " WHERE a.appointment.appointment_id = :appointmentId")
    void deleteByAppointmentId(@Param("appointmentId") String appointmentId);
}
