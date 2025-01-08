package com.dtp.doctor_appointment_booking.dto.DoctorBusy.response;

import com.dtp.doctor_appointment_booking.model.TimeSlot;
import com.dtp.doctor_appointment_booking.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorBusyResponse {
    private int id;
    private String doctor;
    private LocalTime timeSlotFrom;
    private LocalTime timeSlotTo;
    private LocalDate date;
    private String status;
    private String createBy;
}
