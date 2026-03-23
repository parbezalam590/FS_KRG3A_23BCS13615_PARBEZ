package com.example.demo.dto;

import lombok.*;

/**
 * DoctorDTO - Data Transfer Object for Doctor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String specialization;
    private String licenseNumber;
    private Integer yearsOfExperience;
    private String clinic;
    private Long appointmentCount;
}
