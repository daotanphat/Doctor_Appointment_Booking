package com.dtp.doctor_appointment_booking.dto.DoctorBusy.request;

import com.dtp.doctor_appointment_booking.validation.TimeSlotOrder;
import com.dtp.doctor_appointment_booking.validation.ValidTimeSlotOrder;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidTimeSlotOrder(message = "Time slot from must be less than time slot to")
public class CreateDoctorBusyRequest implements TimeSlotOrder {
    private String doctorEmail;

    @FutureOrPresent(message = "Date can not be in the past")
    private LocalDate date;

    private int timeFrom;

    private int timeTo;

    @Override
    public int timeFrom() {
        return timeFrom;
    }

    @Override
    public int timeTo() {
        return timeTo;
    }
}
