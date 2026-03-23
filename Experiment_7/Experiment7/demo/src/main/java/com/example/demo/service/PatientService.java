package com.example.demo.service;

import com.example.demo.dto.PatientDTO;
import com.example.demo.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PatientService - Interface defining patient operations
 */
public interface PatientService {
    
    Patient createPatient(PatientDTO patientDTO);
    
    Optional<Patient> getPatientById(Long id);
    
    Optional<Patient> getPatientByEmail(String email);
    
    /**
     * Pagination: Retrieve patients with offset-based pagination
     */
    Page<Patient> getAllPatients(Pageable pageable);
    
    /**
     * Pagination: Search patients by name
     */
    Page<Patient> searchPatientsByName(String searchTerm, Pageable pageable);
    
    /**
     * Pagination: Get patients created after a specific date
     */
    Page<Patient> getPatientsByCreationDate(LocalDate createdAfter, Pageable pageable);
    
    /**
     * Get patients with medical history
     */
    Page<Patient> getPatientsWithMedicalHistory(Pageable pageable);
    
    /**
     * Get frequently visiting patients (with high appointment count)
     * Helps identify patient segments
     */
    List<Patient> getFrequentPatients(int minimumAppointments);
    
    /**
     * Get active patients (with upcoming appointments)
     */
    Page<Patient> getActivePatients(Pageable pageable);
    
    /**
     * Cursor-based pagination: Get patients after a specific ID
     * Better for real-time data where offsets can be unreliable
     */
    Page<Patient> getPatientsAfterCursor(LocalDate afterDate, Pageable pageable);
    
    Patient updatePatient(Long id, PatientDTO patientDTO);
    
    void deletePatient(Long id);
    
    // Performance reporting
    long getTotalPatientCount();
}
