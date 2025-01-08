package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.DoctorBusy.request.CreateDoctorBusyRequest;
import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.DoctorBusy;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DoctorBusyService {
    DoctorBusy saveDoctorBusy(Appointment appointment, String status);

    DoctorBusy findDoctorBusy(Appointment appointment, String status);

    void deleteDoctorBusy(int doctorBusyId);

    DoctorBusy saveDoctorBusy(CreateDoctorBusyRequest request);
}
