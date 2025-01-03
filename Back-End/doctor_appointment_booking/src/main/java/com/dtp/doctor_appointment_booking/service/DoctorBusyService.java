package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.DoctorBusy;

public interface DoctorBusyService {
    DoctorBusy saveDoctorBusy(Appointment appointment);
}
