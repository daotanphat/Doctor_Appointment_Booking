package com.dtp.doctor_appointment_booking.dto.DoctorBusy.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBusyTimeRequest {
    private LocalDate date;
}
