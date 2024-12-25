package com.dtp.doctor_appointment_booking.service;

public class EndPointService {
    public static final String[] publicPostEndPoint = {
            "/api/auth/signup",
            "/api/auth/signin",
            "/api/auth/refresh-token",
            "/api/auth/logout"
    };

    public static final String[] adminGetEndPoint = {
            "/api/admin/specialities"
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
}
