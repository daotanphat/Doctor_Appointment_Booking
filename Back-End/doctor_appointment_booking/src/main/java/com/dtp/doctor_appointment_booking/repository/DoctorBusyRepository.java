package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Repository
public interface DoctorBusyRepository extends JpaRepository<DoctorBusy, Integer> {
    @Query("SELECT db FROM DoctorBusy db" +
            " WHERE db.doctor.doctor_id = :doctorId" +
            " AND db.date = :date")
    Set<DoctorBusy> findByDoctorIdAndDate(String doctorId, LocalDate date);

    @Query("SELECT db FROM DoctorBusy db" +
            " WHERE db.doctor.doctor_id = :doctorId" +
            " AND db.date = :date" +
            " AND db.timeSlotFrom.time = :from" +
            " AND db.timeSlotTo.time = :to" +
            " AND db.status = :status")
    DoctorBusy findDoctorOccupied(@Param("doctorId") String doctorId,
                                       @Param("date") LocalDate date,
                                       @Param("from") LocalTime from,
                                       @Param("to") LocalTime to,
                                       @Param("status") String status);
}
