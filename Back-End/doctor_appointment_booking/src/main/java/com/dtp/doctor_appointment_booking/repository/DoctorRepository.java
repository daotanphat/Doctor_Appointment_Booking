package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.dto.response.DoctorResponse;
import com.dtp.doctor_appointment_booking.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByEmail(String email);

    @Query("SELECT new com.dtp.doctor_appointment_booking.dto.response.DoctorResponse(d.doctor_id, d.fullName, d.email, " +
            " d.dateOfBirth, d.phone, d.address, d.gender, d.imageUrl, d.status, d.experience, d.degree, d.speciality.name, " +
            " d.fee, d.description, d.bankNumber, d.rating) " +
            " FROM Doctor d " +
            " WHERE (:search IS NULL " +
            " OR LOWER(d.fullName) LIKE LOWER(CONCAT('%', :search))" +
            " OR LOWER(d.speciality.name) LIKE LOWER(CONCAT('%', :search))" +
            " OR LOWER(d.phone) LIKE LOWER(CONCAT('%', :search)))" +
            " AND (:speciality IS NULL OR d.speciality.name = :speciality)")
    Page<DoctorResponse> getDoctorByPageAndFilter(@Param("search") String search,
                                                  @Param("speciality") String speciality,
                                                  Pageable pageable);
}
