package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Doctor Entity - Represents a healthcare professional in HealthHub
 * Demonstrates One-to-Many relationship with Appointments
 */
@Entity
@Table(name = "doctor", indexes = {
    @Index(name = "idx_doc_email", columnList = "email"),
    @Index(name = "idx_specialization", columnList = "specialization"),
    @Index(name = "idx_doc_phone", columnList = "phone_number")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String firstName;
    
    @Column(nullable = false, length = 100)
    private String lastName;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
    
    @Column(nullable = false, length = 100)
    private String specialization;
    
    @Column(length = 20)
    private String licenseNumber;
    
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    
    @Column(length = 255)
    private String clinic;
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    /**
     * One-to-Many relationship with Appointment
     * LAZY fetch to prevent loading all appointments for a doctor
     */
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Appointment> appointments = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDate.now();
    }
}
