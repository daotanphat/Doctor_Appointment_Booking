package com.dtp.doctor_appointment_booking.utils;

import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.service.AppointmentService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class DeleteAppointmentJob implements Job {
    private final AppointmentService appointmentService;

    public DeleteAppointmentJob(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String appointmentId = jobExecutionContext.getJobDetail().getJobDataMap().getString("appointmentId");

        Appointment appointment = appointmentService.getAppointment(appointmentId);
        if (appointment != null && appointment.getPaymentStatus().equals("PENDING")) {
            // Delete appointment
            appointmentService.deleteAppointment(appointmentId);
        }
    }
}
