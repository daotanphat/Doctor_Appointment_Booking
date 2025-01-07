package com.dtp.doctor_appointment_booking.service;

public class EndPointService {
    public static final String[] publicPostEndPoint = {
            "/api/auth/signup",
            "/api/auth/signin",
            "/api/auth/refresh-token",
            "/api/auth/logout",
    };

    public static final String[] publicGetEndPoint = {
            "/api/v1/appointment/payment/{appointmentId}",
    };

    public static final String[] adminGetEndPoint = {
            "/api/admin/specialities",
            "/api/admin/update-doctor-status/{doctorId}",
    };

    public static final String[] adminPostEndPoint = {
            "/api/admin/add-doctor",
            "/api/admin/add-speciality"
    };

    public static final String[] userGetEndPoint = {
            "/api/user/specialities",
            "/api/user/doctor-list",
            "/api/user/doctor/{id}",
            "/api/user/doctor/speciality/{speciality}"
    };

    public static final String[] userGetEndPointAuth = {
            "/api/user/my-info"
    };

    public static final String[] userPostEndPointAuth = {
            "/api/user/update-info"
    };

    public static final String[] appointmentPostEndPointAuth = {
            "/api/v1/appointment/book",
            "/api/v1/appointment/cancel/{appointmentId}"
    };

    public static final String[] appointmentGetEndPointAuth = {
            "/api/v1/appointment/my-appointments",
            "/api/v1/appointment/{appointmentId}/details",
            "/api/v1/appointment/payment_info",
            "/api/v1/appointment/doctor-appointments",
    };
}
