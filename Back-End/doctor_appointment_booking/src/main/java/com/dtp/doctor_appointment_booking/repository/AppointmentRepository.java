package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT a FROM Appointment a" +
            " WHERE a.appointment_id = :appointmentId")
    Optional<Appointment> findByAppointment_id(String appointmentId);

    @Query("SELECT a FROM Appointment a" +
            " JOIN a.statuses s" +
            " WHERE a.doctor.email = :doctorEmail" +
            " AND (:patientEmail IS NULL OR LOWER(a.patient.email) LIKE LOWER(CONCAT('%', :patientEmail, '%')))" +
            " AND (:dateSlot IS NULL OR a.dateSlot = :dateSlot)" +
            " AND (:paymentStatus IS NULL OR a.paymentStatus = :paymentStatus)" +
            " AND (:status IS NULL OR s.status.status = :status)" +
            " ORDER BY CASE WHEN :dateDesc = true THEN a.dateSlot END DESC," +
            " CASE WHEN :dateDesc = false THEN a.dateSlot END ASC")
    Page<Appointment> findAllByDoctor(@Param("doctorEmail") String doctorEmail,
                                      @Param("patientEmail") String patientEmail,
                                      @Param("dateSlot") LocalDate dateSlot,
                                      @Param("paymentStatus") String paymentStatus,
                                      @Param("status") String status,
                                      @Param("dateDesc") boolean dateDesc,
                                      Pageable pageable);

    @Query("SELECT a FROM Appointment a" +
            " JOIN a.statuses s" +
            " WHERE (:status IS NULL OR s.status.status = :status)" +
            " AND (:doctorEmail IS NULL OR a.doctor.email = :doctorEmail)")
    List<Appointment> findNumberAppointmentByStatus(@Param("doctorEmail") String doctorEmail,
                                                    @Param("status") String status);
}
