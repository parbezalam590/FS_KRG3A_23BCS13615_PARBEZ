package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Appointment Entity - Represents an appointment between Patient and Doctor
 * Demonstrates Many-to-One relationships and Eager fetching for core entities
 * Demonstrates N+1 problem when querying without proper fetch strategy
 */
@Entity
@Table(name = "appointment", indexes = {
    @Index(name = "idx_appointment_date", columnList = "appointment_date"),
    @Index(name = "idx_patient_id", columnList = "patient_id"),
    @Index(name = "idx_doctor_id", columnList = "doctor_id"),
    @Index(name = "idx_appointment_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Many-to-One with Patient
     * Using EAGER fetching for accessing patient directly helps avoid lazy loading issues
     * However, when fetching appointments, this can lead to inefficiency
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    /**
     * Many-to-One with Doctor
     * EAGER fetching ensures doctor details are loaded with appointment
     * This can demonstrate the performance trade-offs
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Column(length = 1000)
    private String reason;
    
    @Column(length = 2000)
    private String notes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * One-to-Many with Prescription
     * LAZY loading ensures prescriptions are only loaded when needed
     */
    @OneToMany(mappedBy = "appointment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Prescription> prescriptions = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = AppointmentStatus.SCHEDULED;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum AppointmentStatus {
        SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED, NO_SHOW
    }
}
