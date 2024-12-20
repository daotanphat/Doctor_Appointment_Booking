package com.dtp.doctor_appointment_booking.controller;

import com.dtp.doctor_appointment_booking.dto.request.AddDoctorRequest;
import com.dtp.doctor_appointment_booking.dto.response.MessageResponse;
import com.dtp.doctor_appointment_booking.model.Doctor;
import com.dtp.doctor_appointment_booking.model.Speciality;
import com.dtp.doctor_appointment_booking.service.CloudinaryService;
import com.dtp.doctor_appointment_booking.service.DoctorService;
import com.dtp.doctor_appointment_booking.service.SpecialityService;
import com.dtp.doctor_appointment_booking.utils.ValidationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    DoctorService doctorService;

    @Autowired
    SpecialityService specialityService;

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping("/add-doctor")
    public ResponseEntity<MessageResponse> addDoctor(@RequestPart AddDoctorRequest doctor,
                                                     @RequestPart("image") MultipartFile image) throws JsonProcessingException {
        AddDoctorRequest addDoctorRequest = doctor;
        doctorService.saveDoctor(doctor, image);
        MessageResponse messageResponse = new MessageResponse("Doctor added successfully");

        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/specialities")
    public ResponseEntity<List<Speciality>> getSpecialities() {
        return ResponseEntity.ok(specialityService.getAllSpecialities());
    }

    @PostMapping("/add-speciality")
    public ResponseEntity<MessageResponse> addSpeciality(@RequestParam("name") String name,
                                                         @RequestParam("image") MultipartFile image) {
        if (!ValidationUtil.isValidName(name)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Speciality name must not be null, empty, or contain special characters"));
        }

        specialityService.saveSpeciality(name, image);
        MessageResponse messageResponse = new MessageResponse("Speciality added successfully");
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("folder") String folderName) throws IOException {
        return ResponseEntity.ok(cloudinaryService.uploadFile(file, folderName));
    }
}
