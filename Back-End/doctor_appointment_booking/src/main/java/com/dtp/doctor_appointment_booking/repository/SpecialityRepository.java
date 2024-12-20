package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Integer> {
    Optional<Speciality> findByName(String name);

    List<Speciality> findAll();
}
