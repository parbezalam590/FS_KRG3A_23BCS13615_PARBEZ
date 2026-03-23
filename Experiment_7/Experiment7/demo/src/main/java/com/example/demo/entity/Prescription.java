package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Prescription Entity - Records medications prescribed during appointments
 * Demonstrates Many-to-One relationships with EAGER fetching
 * Part of the HealthHub system for tracking medication history
 */
@Entity
@Table(name = "prescription", indexes = {
    @Index(name = "idx_patient_id_rx", columnList = "patient_id"),
    @Index(name = "idx_appointment_id", columnList = "appointment_id"),
    @Index(name = "idx_prescribed_date", columnList = "prescribed_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;
    
    @Column(nullable = false, length = 100)
    private String medicationName;
    
    @Column(length = 50)
    private String dosage;
    
    @Column(length = 100)
    private String frequency;
    
    @Column(name = "duration_days")
    private Integer durationDays;
    
    @Column(length = 500)
    private String instructions;
    
    @Column(name = "prescribed_date")
    private LocalDate prescribedDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        prescribedDate = LocalDate.now();
        createdAt = LocalDateTime.now();
        if (durationDays != null) {
            expiryDate = prescribedDate.plusDays(durationDays);
        }
    }
}
