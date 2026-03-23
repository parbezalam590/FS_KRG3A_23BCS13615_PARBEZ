package com.example.demo.service.impl;

import com.example.demo.dto.PrescriptionDTO;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * PrescriptionServiceImpl - Implementation of prescription management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {
    
    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    
    @Override
    public Prescription createPrescription(PrescriptionDTO prescriptionDTO) {
        log.info("Creating prescription for patient {} and appointment {}", 
                prescriptionDTO.getPatientId(), prescriptionDTO.getAppointmentId());
        
        Optional<Patient> patientOpt = patientRepository.findById(prescriptionDTO.getPatientId());
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(prescriptionDTO.getAppointmentId());
        
        if (patientOpt.isEmpty() || appointmentOpt.isEmpty()) {
            throw new RuntimeException("Patient or Appointment not found");
        }
        
        Prescription prescription = Prescription.builder()
                .patient(patientOpt.get())
                .appointment(appointmentOpt.get())
                .medicationName(prescriptionDTO.getMedicationName())
                .dosage(prescriptionDTO.getDosage())
                .frequency(prescriptionDTO.getFrequency())
                .durationDays(prescriptionDTO.getDurationDays())
                .instructions(prescriptionDTO.getInstructions())
                .build();
        
        return prescriptionRepository.save(prescription);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getPatientPrescriptionHistory(Long patientId) {
        log.info("Fetching prescription history for patient: {}", patientId);
        
        return prescriptionRepository.findPatientPrescriptionHistory(patientId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PrescriptionDTO> getActivePrescriptions(Pageable pageable) {
        log.info("Fetching active prescriptions");
        
        return prescriptionRepository.findActivePrescriptions(pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PrescriptionDTO> getExpiredPrescriptions(Pageable pageable) {
        log.info("Fetching expired prescriptions");
        
        return prescriptionRepository.findExpiredPrescriptions(pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PrescriptionDTO> searchByMedicationName(String medicationName, Pageable pageable) {
        log.info("Searching prescriptions by medication: {}", medicationName);
        
        return prescriptionRepository.findByMedicationName(medicationName, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getPrescriptionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching prescriptions between {} and {}", startDate, endDate);
        
        return prescriptionRepository.findPrescriptionsBeteenDates(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Prescription updatePrescription(Long id, PrescriptionDTO prescriptionDTO) {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
        if (prescriptionOpt.isEmpty()) {
            throw new RuntimeException("Prescription not found");
        }
        
        Prescription prescription = prescriptionOpt.get();
        prescription.setMedicationName(prescriptionDTO.getMedicationName());
        prescription.setDosage(prescriptionDTO.getDosage());
        prescription.setFrequency(prescriptionDTO.getFrequency());
        prescription.setDurationDays(prescriptionDTO.getDurationDays());
        prescription.setInstructions(prescriptionDTO.getInstructions());
        
        return prescriptionRepository.save(prescription);
    }
    
    @Override
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
    
    private PrescriptionDTO convertToDTO(Prescription prescription) {
        LocalDate expiryDate = prescription.getExpiryDate();
        boolean isActive = expiryDate != null && expiryDate.isAfter(LocalDate.now());
        
        return PrescriptionDTO.builder()
                .id(prescription.getId())
                .patientId(prescription.getPatient().getId())
                .appointmentId(prescription.getAppointment().getId())
                .medicationName(prescription.getMedicationName())
                .dosage(prescription.getDosage())
                .frequency(prescription.getFrequency())
                .durationDays(prescription.getDurationDays())
                .instructions(prescription.getInstructions())
                .prescribedDate(prescription.getPrescribedDate())
                .expiryDate(expiryDate)
                .isActive(isActive)
                .build();
    }
}
