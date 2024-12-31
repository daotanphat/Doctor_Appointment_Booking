package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface DoctorBusyRepository extends JpaRepository<DoctorBusy, Integer> {
    @Query("SELECT db FROM DoctorBusy db" +
            " WHERE db.doctor.doctor_id = :doctorId" +
            " AND db.date = :date")
    Set<DoctorBusy> findByDoctorIdAndDate(String doctorId, LocalDate date);
}
