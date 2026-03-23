package com.example.demo.dto;

import lombok.*;
import java.time.LocalDate;

/**
 * PatientDTO - Data Transfer Object for Patient
 * Separates DTOs from entities to control what data is exposed
 * Includes nested objects selectively
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String medicalHistory;
    private String address;
    private Integer appointmentCount;
}
