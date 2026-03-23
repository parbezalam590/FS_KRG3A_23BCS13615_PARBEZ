package com.example.demo.controller;

import com.example.demo.dto.DoctorDTO;
import com.example.demo.entity.Doctor;
import com.example.demo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * DoctorController - REST API for doctor management
 */
@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {
    
    private final DoctorService doctorService;
    
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        return ResponseEntity.status(201).body(doctorService.createDoctor(doctorDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        return doctor.isPresent() ? 
                ResponseEntity.ok(doctor.get()) : 
                ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public ResponseEntity<Page<Doctor>> getAllDoctors(Pageable pageable) {
        return ResponseEntity.ok(doctorService.getAllDoctors(pageable));
    }
    
    /**
     * Find doctors by specialization
     * Usage: GET /api/v1/doctors/specialization/Cardiology
     */
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<Page<Doctor>> getDoctorsBySpecialization(
            @PathVariable String specialization,
            Pageable pageable) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specialization, pageable));
    }
    
    /**
     * Find available doctors (with fewer appointments)
     * Usage: GET /api/v1/doctors/available?maxAppointments=20
     */
    @GetMapping("/available")
    public ResponseEntity<List<DoctorDTO>> getAvailableDoctors(
            @RequestParam(defaultValue = "10") long maxAppointments) {
        return ResponseEntity.ok(doctorService.getAvailableDoctors(maxAppointments));
    }
    
    /**
     * Find doctors by experience level
     * Usage: GET /api/v1/doctors/experience?minYears=5&maxYears=20
     */
    @GetMapping("/experience")
    public ResponseEntity<Page<Doctor>> getDoctorsByExperience(
            @RequestParam Integer minYears,
            @RequestParam Integer maxYears,
            Pageable pageable) {
        return ResponseEntity.ok(doctorService.getDoctorsByExperience(minYears, maxYears, pageable));
    }
    
    /**
     * Find doctors by specialization and experience
     * Usage: GET /api/v1/doctors/filter?specialization=Cardiology&minExp=10
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<Doctor>> getDoctorsByFilter(
            @RequestParam String specialization,
            @RequestParam Integer minExp,
            Pageable pageable) {
        return ResponseEntity.ok(
                doctorService.getDoctorsBySpecializationAndExperience(specialization, minExp, pageable));
    }
    
    /**
     * Get most experienced doctors
     */
    @GetMapping("/experienced")
    public ResponseEntity<List<Doctor>> getMostExperiencedDoctors(Pageable pageable) {
        return ResponseEntity.ok(doctorService.getMostExperiencedDoctors(pageable));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorDTO doctorDTO) {
        try {
            return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
