package com.dtp.doctor_appointment_booking.dto.appointment;

import com.dtp.doctor_appointment_booking.validation.TimeSlotOrder;
import com.dtp.doctor_appointment_booking.validation.ValidTimeSlotOrder;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidTimeSlotOrder(message = "Time slot from must be less than time slot to")
public class BookAppointmentRequest implements TimeSlotOrder {
    private String doctor_id;

    private String patient_mail;

    @Positive(message = "Fee must be larger than 0")
    private float fee;

    @FutureOrPresent(message = "Date can not be in the past")
    private LocalDate dateSlot;

    private int timeSlotFrom;

    private int timeSlotTo;

    @NotBlank(message = "Address cannot be null")
    @Size(min = 3, message = "Address must be at least 3 characters long")
    private String address;

    @Override
    public int timeFrom() {
        return timeSlotFrom;
    }

    @Override
    public int timeTo() {
        return timeSlotTo;
    }
}
