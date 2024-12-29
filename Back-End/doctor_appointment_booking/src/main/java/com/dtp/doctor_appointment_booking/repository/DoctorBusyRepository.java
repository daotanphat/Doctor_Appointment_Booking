package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface DoctorBusyRepository extends JpaRepository<DoctorBusy, Integer> {
    Set<DoctorBusy> findByDoctor_Doctor_idAndAndDate(String doctorId, LocalDate date);
}
