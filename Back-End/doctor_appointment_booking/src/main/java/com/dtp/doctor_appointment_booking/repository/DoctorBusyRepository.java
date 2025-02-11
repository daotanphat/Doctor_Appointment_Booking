package com.dtp.doctor_appointment_booking.repository;

import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DoctorBusyRepository extends JpaRepository<DoctorBusy, Integer> {
    @Query("SELECT db FROM DoctorBusy db" +
            " WHERE db.doctor.doctor_id = :doctorId" +
            " AND db.date = :date")
    List<DoctorBusy> findByDoctorIdAndDate(String doctorId, LocalDate date);

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

    @Query("SELECT db FROM DoctorBusy db" +
            " JOIN db.createBy.roles r" +
            " WHERE r.name = :doctor" +
            " AND db.createBy.id = :doctorId" +
            " AND db.date = :date")
    List<DoctorBusy> findByDoctorCreatedAndDate(@Param("doctorId") int doctorId,
                                                @Param("date") LocalDate date,
                                                @Param("doctor") String doctorRole);

    @Query("SELECT db FROM DoctorBusy db" +
            " WHERE db.doctor.id = : doctorId" +
            " AND db.date = :date" +
            " AND db.timeSlotFrom.time = :timeFrom" +
            " AND db.timeSlotTo.time = :timeTo")
    DoctorBusy findByDoctorAndDateAndTimeSlot(@Param("doctorId") int doctorId,
                                              @Param("date") LocalDate date,
                                              @Param("timeFrom") LocalTime timeFrom,
                                              @Param("timeTo") LocalTime timeTo);

    @Query("SELECT db FROM DoctorBusy db" +
            " WHERE db.date < :date")
    List<DoctorBusy> findBusyTimeInThePast(@Param("date") LocalDate date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT db FROM DoctorBusy db" +
            " WHERE db.doctor.id = :doctorId" +
            " AND db.date = :date" +
            " AND (db.timeSlotFrom.time < :timeTo AND db.timeSlotTo.time > :timeFrom)")
    List<DoctorBusy> findBusyTimeConflict(@Param("doctorId") int doctorId,
                                          @Param("date") LocalDate date,
                                          @Param("timeFrom") LocalTime timeFrom,
                                          @Param("timeTo") LocalTime timeTo);
}
