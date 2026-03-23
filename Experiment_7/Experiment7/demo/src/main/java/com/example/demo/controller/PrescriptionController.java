package com.example.demo.controller;

import com.example.demo.dto.PrescriptionDTO;
import com.example.demo.entity.Prescription;
import com.example.demo.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PrescriptionController - REST API for prescription management
 */
@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    
    private final PrescriptionService prescriptionService;
    
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody PrescriptionDTO prescriptionDTO) {
        return ResponseEntity.status(201).body(prescriptionService.createPrescription(prescriptionDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable Long id) {
        Optional<Prescription> prescription = prescriptionService.getPrescriptionById(id);
        return prescription.isPresent() ? 
                ResponseEntity.ok(prescription.get()) : 
                ResponseEntity.notFound().build();
    }
    
    /**
     * Get patient's prescription history
     * Usage: GET /api/v1/prescriptions/patient/1
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDTO>> getPatientPrescriptionHistory(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPatientPrescriptionHistory(patientId));
    }
    
    /**
     * Get active prescriptions (not yet expired)
     * Usage: GET /api/v1/prescriptions/active?page=0&size=10
     */
    @GetMapping("/active")
    public ResponseEntity<Page<PrescriptionDTO>> getActivePrescriptions(Pageable pageable) {
        return ResponseEntity.ok(prescriptionService.getActivePrescriptions(pageable));
    }
    
    /**
     * Get expired prescriptions
     */
    @GetMapping("/expired")
    public ResponseEntity<Page<PrescriptionDTO>> getExpiredPrescriptions(Pageable pageable) {
        return ResponseEntity.ok(prescriptionService.getExpiredPrescriptions(pageable));
    }
    
    /**
     * Search prescriptions by medication name
     * Usage: GET /api/v1/prescriptions/search?medication=Aspirin
     */
    @GetMapping("/search")
    public ResponseEntity<Page<PrescriptionDTO>> searchByMedication(
            @RequestParam String medication,
            Pageable pageable) {
        return ResponseEntity.ok(prescriptionService.searchByMedicationName(medication, pageable));
    }
    
    /**
     * Get prescriptions issued between dates
     */
    @GetMapping("/between")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsBetweenDates(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsBetweenDates(
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(
            @PathVariable Long id,
            @RequestBody PrescriptionDTO prescriptionDTO) {
        try {
            return ResponseEntity.ok(prescriptionService.updatePrescription(id, prescriptionDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
