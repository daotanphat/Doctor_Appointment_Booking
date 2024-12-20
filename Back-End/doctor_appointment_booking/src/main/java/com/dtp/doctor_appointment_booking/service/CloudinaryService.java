package com.dtp.doctor_appointment_booking.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public Map uploadFile(MultipartFile file, String folderName) throws IOException {
        return cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("resource_type", "image", "folder", folderName));
    }

    public Map uploadVideo(MultipartFile file, String folderName) throws IOException {
        return cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("resource_type", "video", "folder", folderName));
    }
}
