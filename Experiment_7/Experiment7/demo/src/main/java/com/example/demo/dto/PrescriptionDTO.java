package com.example.demo.dto;

import lombok.*;
import java.time.LocalDate;

/**
 * PrescriptionDTO - Data Transfer Object for Prescription
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDTO {
    private Long id;
    private Long patientId;
    private Long appointmentId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private Integer durationDays;
    private String instructions;
    private LocalDate prescribedDate;
    private LocalDate expiryDate;
    private boolean isActive;
}
