package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;

/**
 * AppointmentDTO - Data Transfer Object for Appointment
 * Includes nested patient and doctor info selectively
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentDTO {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentDate;
    private Integer durationMinutes;
    private String reason;
    private String notes;
    private String status;
    
    // Nested objects (used selectively to avoid over-fetching)
    private PatientDTO patient;
    private DoctorDTO doctor;
    
    // Prescription count for quick info
    private Integer prescriptionCount;
}
