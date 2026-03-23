package com.example.demo.controller;

import com.example.demo.dto.PatientDTO;
import com.example.demo.entity.Patient;
import com.example.demo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PatientController - REST API for patient management
 * Demonstrates pagination, search, and cursor-based pagination
 */
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {
    
    private final PatientService patientService;
    
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody PatientDTO patientDTO) {
        return ResponseEntity.status(201).body(patientService.createPatient(patientDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        return patient.isPresent() ? 
                ResponseEntity.ok(patient.get()) : 
                ResponseEntity.notFound().build();
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getPatientByEmail(@PathVariable String email) {
        Optional<Patient> patient = patientService.getPatientByEmail(email);
        return patient.isPresent() ? 
                ResponseEntity.ok(patient.get()) : 
                ResponseEntity.notFound().build();
    }
    
    /**
     * Standard pagination example
     * Usage: GET /api/v1/patients?page=0&size=10&sort=id,desc
     */
    @GetMapping
    public ResponseEntity<Page<Patient>> getAllPatients(Pageable pageable) {
        return ResponseEntity.ok(patientService.getAllPatients(pageable));
    }
    
    /**
     * Search patients by name with pagination
     * Usage: GET /api/v1/patients/search?term=john&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Patient>> searchPatients(
            @RequestParam String term,
            Pageable pageable) {
        return ResponseEntity.ok(patientService.searchPatientsByName(term, pageable));
    }
    
    /**
     * Get patients created after a date (cursor-based pagination approach)
     * Usage: GET /api/v1/patients/active?after=2024-01-01&page=0&size=20
     */
    @GetMapping("/active")
    public ResponseEntity<Page<Patient>> getActivePatients(Pageable pageable) {
        return ResponseEntity.ok(patientService.getActivePatients(pageable));
    }
    
    /**
     * Get patients with medical history
     */
    @GetMapping("/with-history")
    public ResponseEntity<Page<Patient>> getPatientsWithHistory(Pageable pageable) {
        return ResponseEntity.ok(patientService.getPatientsWithMedicalHistory(pageable));
    }
    
    /**
     * Get frequently visiting patients
     * Usage: GET /api/v1/patients/frequent?minAppointments=5
     */
    @GetMapping("/frequent")
    public ResponseEntity<List<Patient>> getFrequentPatients(
            @RequestParam(defaultValue = "3") int minAppointments) {
        return ResponseEntity.ok(patientService.getFrequentPatients(minAppointments));
    }
    
    /**
     * Statistics endpoint
     */
    @GetMapping("/stats/count")
    public ResponseEntity<?> getPatientCount() {
        return ResponseEntity.ok(new Object() {
            public final long totalPatients = patientService.getTotalPatientCount();
        });
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientDTO patientDTO) {
        try {
            return ResponseEntity.ok(patientService.updatePatient(id, patientDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
