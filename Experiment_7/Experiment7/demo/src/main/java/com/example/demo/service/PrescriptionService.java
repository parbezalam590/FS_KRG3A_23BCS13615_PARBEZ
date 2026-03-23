package com.example.demo.service;

import com.example.demo.dto.PrescriptionDTO;
import com.example.demo.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PrescriptionService - Interface for prescription management
 */
public interface PrescriptionService {
    
    Prescription createPrescription(PrescriptionDTO prescriptionDTO);
    
    Optional<Prescription> getPrescriptionById(Long id);
    
    /**
     * Get patient's prescription history
     */
    List<PrescriptionDTO> getPatientPrescriptionHistory(Long patientId);
    
    /**
     * Get active prescriptions (not yet expired)
     */
    Page<PrescriptionDTO> getActivePrescriptions(Pageable pageable);
    
    /**
     * Get expired prescriptions
     */
    Page<PrescriptionDTO> getExpiredPrescriptions(Pageable pageable);
    
    /**
     * Search prescriptions by medication name
     */
    Page<PrescriptionDTO> searchByMedicationName(String medicationName, Pageable pageable);
    
    /**
     * Get prescriptions issued between dates
     */
    List<PrescriptionDTO> getPrescriptionsBetweenDates(LocalDate startDate, LocalDate endDate);
    
    Prescription updatePrescription(Long id, PrescriptionDTO prescriptionDTO);
    
    void deletePrescription(Long id);
}
