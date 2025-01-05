package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.config.VnPayConfig;
import com.dtp.doctor_appointment_booking.dto.email.EmailDetails;
import com.dtp.doctor_appointment_booking.dto.payment.PaymentResponse;
import com.dtp.doctor_appointment_booking.dto.payment.PaymentStatusResponse;
import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import com.dtp.doctor_appointment_booking.model.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VnPayService {
    private final AppointmentService appointmentService;
    private final DoctorBusyService doctorBusyService;
    private final EmailService emailService;

    public VnPayService(AppointmentService appointmentService, DoctorBusyService doctorBusyService,
                        EmailService emailService) {
        this.appointmentService = appointmentService;
        this.doctorBusyService = doctorBusyService;
        this.emailService = emailService;
    }

    public PaymentResponse createPayment(String appointmentId, HttpServletRequest request) throws UnsupportedEncodingException {
        Appointment appointment = appointmentService.getAppointment(appointmentId);
//        String orderInfo = "Appointment_" + appointment.getAppointment_id()
//                + "_" + appointment.getPatient().getFullName()
//                + "_" + appointment.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String orderInfo = appointmentId;

        long amount = (long) (appointment.getFee() * 100);
        String orderType = "SUCCESS";
        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnPayConfig.getIpAddress(request);
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus("OK");
        paymentResponse.setMessage("Successfully");
        paymentResponse.setURL(paymentUrl);

        return paymentResponse;
    }

    public PaymentStatusResponse vnPayResponse(HttpServletRequest request) throws Exception {
        PaymentStatusResponse paymentStatusResponse = new PaymentStatusResponse();

        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }

        // Check checksum
        String signValue = VnPayConfig.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            String orderInfo = request.getParameter("vnp_OrderInfo");
            String appointmentId = orderInfo.split("_")[1];
            Appointment appointment = appointmentService.getAppointment(appointmentId);

            boolean checkOrderId = appointment != null;
            boolean checkAmount = appointment.getFee() == Float.parseFloat(request.getParameter("vnp_Amount"));
            boolean checkOrderStatus = PaymentStatus.PENDING.toString().equals(appointment.getPaymentStatus());

            if (checkOrderId) {
                if (checkAmount) {
                    if (checkOrderStatus) {
                        if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                            // Update payment status to success
                            appointmentService.updatePaymentAppointment(appointmentId, PaymentStatus.SUCCESS.toString());
                            // Update appointment status to success
                            appointmentService.updateAppointmentStatus(appointmentId, "SUCCESS");
                            // update doctor busy time
                            doctorBusyService.saveDoctorBusy(appointment, "BUSY");
                            // send email to doctor and patient
                            emailService.sendAppointmentNotification(appointment);

                            paymentStatusResponse.setRspCode("00");
                            paymentStatusResponse.setMessage("Payment success");
                        }
                    } else {
                        appointmentService.updatePaymentAppointment(appointmentId, PaymentStatus.FAILED.toString());

                        paymentStatusResponse.setRspCode("02");
                        paymentStatusResponse.setMessage("Payment failed");
                    }
                } else {
                    paymentStatusResponse.setRspCode("05");
                    paymentStatusResponse.setMessage("Invalid Amount");
                }
            } else {
                paymentStatusResponse.setRspCode("01");
                paymentStatusResponse.setMessage("Order not Found");
            }
        } else {
            paymentStatusResponse.setRspCode("97");
            paymentStatusResponse.setMessage("Invalid Checksum");
        }
        return paymentStatusResponse;
    }
}
