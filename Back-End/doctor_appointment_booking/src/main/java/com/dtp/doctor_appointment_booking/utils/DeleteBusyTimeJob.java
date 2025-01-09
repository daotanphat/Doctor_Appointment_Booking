package com.dtp.doctor_appointment_booking.utils;

import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import com.dtp.doctor_appointment_booking.repository.DoctorBusyRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteBusyTimeJob implements Job {
    private final DoctorBusyRepository doctorBusyRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<DoctorBusy> doctorBusies = doctorBusyRepository.findBusyTimeInThePast(LocalDate.now());

        doctorBusyRepository.deleteAll(doctorBusies);
    }
}
