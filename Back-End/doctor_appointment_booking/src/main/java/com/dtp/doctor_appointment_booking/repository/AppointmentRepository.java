package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a FROM Appointment a" +
            " WHERE a.patient.email = :email" +
            " AND (:search IS NULL " +
            " OR LOWER(a.doctor.fullName) LIKE LOWER(CONCAT('%', :search, '%'))" +
            " OR LOWER(a.doctor.speciality.name) LIKE LOWER(CONCAT('%', :search, '%')))" +
            " ORDER BY a.createdAt DESC")
    Page<Appointment> findAllByPatientEmail(@Param("email") String email,
                                            @Param("search") String search,
                                            Pageable pageable);
}
