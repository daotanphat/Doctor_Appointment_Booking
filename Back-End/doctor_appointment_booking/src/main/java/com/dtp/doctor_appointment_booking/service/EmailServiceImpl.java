package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.email.EmailDetails;
import com.dtp.doctor_appointment_booking.model.Appointment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendAppointmentNotification(Appointment appointment) {
        String doctorName = appointment.getDoctor().getFullName();
        String patientName = appointment.getPatient().getFullName();
        String doctorEmail = appointment.getDoctor().getEmail();
        String patientEmail = appointment.getPatient().getEmail();
        String appointmentDate = appointment.getDateSlot().toString();
        String appointmentTime = appointment.getTimeSlotFrom().getTime() + "_" + appointment.getTimeSlotTo().getTime();
        String location = appointment.getAddress();
        String subject = "Appointment_" + appointmentDate + "_" + appointmentTime;

        String msgBodyToDoctor = "<html>" +
                "<body style=\"font-family: Arial, sans-serif;\">" +
                "<h2 style=\"color: #4CAF50;\">New Appointment Notification</h2>" +
                "<p>Dear Dr. " + doctorName + ",</p>" +
                "<p>You have a new appointment scheduled with the following details:</p>" +
                "<table style=\"border-collapse: collapse; width: 100%; margin: 20px 0;\">" +
                "<tr>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\"><strong>Patient:</strong></td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + patientName + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\"><strong>Appointment Date:</strong></td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + appointmentDate + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\"><strong>Appointment Time:</strong></td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + appointmentTime + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\"><strong>Location:</strong></td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + location + "</td>" +
                "</tr>" +
                "</table>" +
                "<p>Please make sure to confirm or reschedule the appointment if needed. You can contact the patient at " + patientEmail + ".</p>" +
                "<p>Thank you for your time!</p>" +
                "<p>Best regards,<br/>Prescripto System</p>" +
                "</body>" +
                "</html>";

        // Prepare the HTML body for the email to patient
        String msgBodyToPatient = "<html>" +
                "<body style=\"font-family: Arial, sans-serif;\">" +
                "<h2 style=\"color: #4CAF50;\">Appointment Confirmation</h2>" +
                "<p>Dear " + patientName + ",</p>" +
                "<p>Your appointment with Dr. " + doctorName + " has been scheduled with the following details:</p>" +
                "<table style=\"border-collapse: collapse; width: 100%; margin: 20px 0;\">" +
                "<tr>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\"><strong>Doctor:</strong></td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + doctorName + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\"><strong>Appointment Date:</strong></td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + appointmentDate + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\"><strong>Appointment Time:</strong></td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + appointmentTime + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\"><strong>Location:</strong></td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + location + "</td>" +
                "</tr>" +
                "</table>" +
                "<p>If you have any questions or need to reschedule, please contact Dr. " + doctorName + " directly.</p>" +
                "<p>Thank you!</p>" +
                "<p>Best regards,<br/>Your Appointment System Team</p>" +
                "</body>" +
                "</html>";

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> sendSimpleMail(doctorEmail, subject, msgBodyToDoctor));
        executorService.submit(() -> sendSimpleMail(patientEmail, subject, msgBodyToPatient));

        executorService.shutdown();
    }

    public void sendSimpleMail(String recipient, String subject, String msgBody) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Set the email fields
            helper.setFrom(sender);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(msgBody, true);  // Set to true to indicate HTML content

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
