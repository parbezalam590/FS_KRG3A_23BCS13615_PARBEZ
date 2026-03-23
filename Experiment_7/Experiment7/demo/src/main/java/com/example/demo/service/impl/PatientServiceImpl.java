package com.example.demo.service.impl;

import com.example.demo.dto.PatientDTO;
import com.example.demo.entity.Patient;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PatientServiceImpl - Implementation of patient operations
 * Demonstrates proper pagination, cursor-based approaches, and performance monitoring
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientServiceImpl implements PatientService {
    
    private final PatientRepository patientRepository;
    
    @Override
    public Patient createPatient(PatientDTO patientDTO) {
        log.info("Creating new patient: {}", patientDTO.getEmail());
        long startTime = System.currentTimeMillis();
        
        Patient patient = Patient.builder()
                .firstName(patientDTO.getFirstName())
                .lastName(patientDTO.getLastName())
                .email(patientDTO.getEmail())
                .phoneNumber(patientDTO.getPhoneNumber())
                .dateOfBirth(patientDTO.getDateOfBirth())
                .medicalHistory(patientDTO.getMedicalHistory())
                .address(patientDTO.getAddress())
                .build();
        
        Patient savedPatient = patientRepository.save(patient);
        
        long duration = System.currentTimeMillis() - startTime;
        log.info("Patient created in {} ms with ID: {}", duration, savedPatient.getId());
        
        return savedPatient;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> getPatientById(Long id) {
        log.debug("Fetching patient by ID: {}", id);
        return patientRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> getPatientByEmail(String email) {
        log.debug("Fetching patient by email: {}", email);
        return patientRepository.findByEmail(email);
    }
    
    /**
     * Standard offset-based pagination
     * Good for UI components with page numbers
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> getAllPatients(Pageable pageable) {
        log.info("Fetching all patients, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        long startTime = System.currentTimeMillis();
        
        Page<Patient> patientsPage = patientRepository.findAll(pageable);
        
        long duration = System.currentTimeMillis() - startTime;
        log.info("Retrieved {} patients in {} ms", patientsPage.getNumberOfElements(), duration);
        
        return patientsPage;
    }
    
    /**
     * Search with pagination
     * Demonstrates using LIKE queries for search functionality
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> searchPatientsByName(String searchTerm, Pageable pageable) {
        log.info("Searching patients by name: {}", searchTerm);
        long startTime = System.currentTimeMillis();
        
        Page<Patient> results = patientRepository.searchPatientsByName(searchTerm, pageable);
        
        long duration = System.currentTimeMillis() - startTime;
        log.info("Found {} patients matching '{}' in {} ms", results.getTotalElements(), searchTerm, duration);
        
        return results;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> getPatientsByCreationDate(LocalDate createdAfter, Pageable pageable) {
        log.info("Fetching patients created after: {}", createdAfter);
        return patientRepository.findPatientsCreatedAfter(createdAfter, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> getPatientsWithMedicalHistory(Pageable pageable) {
        log.info("Fetching patients with medical history");
        return patientRepository.findPatientsWithMedicalHistory(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Patient> getFrequentPatients(int minimumAppointments) {
        log.info("Fetching patients with at least {} appointments", minimumAppointments);
        return patientRepository.findPatientsByMinimumAppointments(minimumAppointments);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> getActivePatients(Pageable pageable) {
        log.info("Fetching active patients");
        return patientRepository.findActivePatients(pageable);
    }
    
    /**
     * Cursor-based pagination
     * Better for high-volume data as it doesn't skip records
     * Suitable for "load more" scenarios in mobile apps
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> getPatientsAfterCursor(LocalDate afterDate, Pageable pageable) {
        log.info("Fetching patients after cursor date: {}", afterDate);
        return patientRepository.findPatientsCreatedAfter(afterDate, pageable);
    }
    
    @Override
    public Patient updatePatient(Long id, PatientDTO patientDTO) {
        log.info("Updating patient with ID: {}", id);
        
        Optional<Patient> patientOpt = patientRepository.findById(id);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found with ID: " + id);
        }
        
        Patient patient = patientOpt.get();
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setEmail(patientDTO.getEmail());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setMedicalHistory(patientDTO.getMedicalHistory());
        patient.setAddress(patientDTO.getAddress());
        
        return patientRepository.save(patient);
    }
    
    @Override
    public void deletePatient(Long id) {
        log.info("Deleting patient with ID: {}", id);
        patientRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalPatientCount() {
        return patientRepository.count();
    }
}
