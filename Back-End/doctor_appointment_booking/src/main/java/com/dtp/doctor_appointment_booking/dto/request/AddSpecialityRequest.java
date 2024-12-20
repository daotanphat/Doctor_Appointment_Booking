package com.dtp.doctor_appointment_booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSpecialityRequest {
    @NotBlank(message = "Name cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must not include special characters & numbers")
    private String name;

    @NotBlank(message = "Image cannot be null or empty")
    private String imageUrl;
}
