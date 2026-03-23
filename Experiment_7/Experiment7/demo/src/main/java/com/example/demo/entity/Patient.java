package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Patient Entity - Represents a patient in the HealthHub system
 * Demonstrates One-to-Many relationship with Appointments
 * Uses Lazy fetching for appointment collection
 */
@Entity
@Table(name = "patient", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_phone", columnList = "phone_number"),
    @Index(name = "idx_dob", columnList = "date_of_birth")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String firstName;
    
    @Column(nullable = false, length = 100)
    private String lastName;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(name = "phone_number", unique = true, nullable = false, length = 20)
    private String phoneNumber;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(length = 500)
    private String medicalHistory;
    
    @Column(length = 255)
    private String address;
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    /**
     * One-to-Many relationship with Appointment
     * Using LAZY fetching to avoid N+1 problems
     * orphanRemoval ensures when appointment is removed, it's deleted from DB
     */
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Appointment> appointments = new HashSet<>();
    
    /**
     * Relationship with Prescription records
     */
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Prescription> prescriptions = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
