package com.dtp.doctor_appointment_booking.utils;

import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class Schedule {
    private final SchedulerFactoryBean schedulerFactoryBean;

    public Schedule(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    public void ScheduleDeleteAppointmentJob(String appointmentId) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder.newJob(DeleteAppointmentJob.class)
                    .withIdentity("DeleteAppointmentJob_" + appointmentId, "Appointments")
                    .usingJobData("appointmentId", appointmentId)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("DeleteTrigger_" + appointmentId, "Appointments")
                    .startAt(DateBuilder.futureDate(10, DateBuilder.IntervalUnit.MINUTE))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Error delete appointment not payment job");
        }
    }

    @PostConstruct
    public void scheduleDeleteBusyTimeInThePast() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder.newJob(DeleteBusyTimeJob.class)
                    .withIdentity("DeleteBusyTimeInThePast", "DailyJobs")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("DailyTrigger", "DailyJobs")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 34))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Error delete busy time which is in the past");
        }
    }
}
