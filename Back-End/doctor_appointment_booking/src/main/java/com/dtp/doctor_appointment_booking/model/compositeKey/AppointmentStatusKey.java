package com.dtp.doctor_appointment_booking.model.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusKey implements Serializable {
    @Column(name = "appointment_id")
    private int appointmentId;

    @Column(name = "status_id")
    private int statusId;
}
