package com.example.demo.service.impl;

import com.example.demo.dto.AppointmentDTO;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.PerformanceMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AppointmentServiceImpl - Implementation demonstrating N+1 problem and solutions
 * 
 * Key Concepts Demonstrated:
 * 1. N+1 Problem: When fetching appointments naively without JOIN FETCH,
 *    we get 1 query for all appointments + N queries for each patient + N queries for each doctor = 2N+1 queries
 * 
 * 2. Solution: Using JOIN FETCH to load related entities in a single query
 * 
 * 3. Performance Comparison: comparePerformance() method shows the actual difference
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    
    @Override
    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        log.info("Creating appointment for patient {} and doctor {}", 
                appointmentDTO.getPatientId(), appointmentDTO.getDoctorId());
        
        Optional<Patient> patientOpt = patientRepository.findById(appointmentDTO.getPatientId());
        Optional<Doctor> doctorOpt = doctorRepository.findById(appointmentDTO.getDoctorId());
        
        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            throw new RuntimeException("Patient or Doctor not found");
        }
        
        Appointment appointment = Appointment.builder()
                .patient(patientOpt.get())
                .doctor(doctorOpt.get())
                .appointmentDate(appointmentDTO.getAppointmentDate())
                .durationMinutes(appointmentDTO.getDurationMinutes())
                .reason(appointmentDTO.getReason())
                .notes(appointmentDTO.getNotes())
                .build();
        
        return appointmentRepository.save(appointment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
    
    /**
     * ⚠️ PROBLEMATIC: Demonstrates N+1 Problem
     * 
     * Query Flow:
     * 1. SELECT * FROM appointment WHERE status = 'SCHEDULED' = 1 QUERY
     * 2. For each appointment, load Patient = N QUERIES
     * 3. For each appointment, load Doctor = N QUERIES
     * Total = 2N + 1 queries
     * 
     * If you have 100 appointments:
     * - 1 query to get all appointments
     * - 100 queries to get each patient
     * - 100 queries to get each doctor
     * = 201 QUERIES!
     */
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointmentsNaive() {
        log.warn("⚠️ NAIVE QUERY: This will cause N+1 problem!");
        
        long startTime = System.nanoTime();
        
        // This query loads all appointments WITHOUT patient and doctor
        List<Appointment> appointments = appointmentRepository.findByStatusBasic(
                Appointment.AppointmentStatus.SCHEDULED);
        
        log.warn("Naive query found {} appointments (without related entities loaded)", appointments.size());
        
        // Now convert to DTO - accessing patient and doctor will trigger LAZY loading
        // Since appointments were loaded with EAGER fetch on relationships but we didn't use JOIN FETCH,
        // each access to patient/doctor will cause a new query
        List<AppointmentDTO> dtos = appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        long duration = System.nanoTime() - startTime;
        log.error("⚠️ Naive approach took {} ms for {} appointments - N+1 PROBLEM!", 
                duration / 1_000_000, appointments.size());
        
        return dtos;
    }
    
    /**
     * ✅ OPTIMIZED: Solves N+1 Problem using JOIN FETCH
     * 
     * Query Flow:
     * 1. SELECT a.*, p.*, d.* FROM appointment a 
     *    LEFT JOIN FETCH appointment.patient p 
     *    LEFT JOIN FETCH appointment.doctor d 
     *    WHERE status = 'SCHEDULED'
     * = 1 QUERY
     * 
     * All data is loaded in a single query with JOINs!
     * Huge performance improvement, especially with large datasets.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointmentsOptimized() {
        log.info("✅ OPTIMIZED QUERY: Using JOIN FETCH to prevent N+1");
        
        long startTime = System.nanoTime();
        
        // This query uses JOIN FETCH to load all relationships in a single query
        List<Appointment> appointments = appointmentRepository.findByStatusOptimized(
                Appointment.AppointmentStatus.SCHEDULED);
        
        List<AppointmentDTO> dtos = appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        long duration = System.nanoTime() - startTime;
        log.info("✅ Optimized approach took {} ms for {} appointments - SINGLE QUERY!", 
                duration / 1_000_000, appointments.size());
        
        return dtos;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAppointmentsByPatientId(Long patientId, Pageable pageable) {
        log.info("Fetching appointments for patient: {}", patientId);
        
        return appointmentRepository.findByPatientId(patientId, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAppointmentsByDoctorId(Long doctorId, Pageable pageable) {
        log.info("Fetching appointments for doctor: {}", doctorId);
        
        return appointmentRepository.findByDoctorId(doctorId, pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * Cursor-based pagination: Get appointments after a specific datetime
     * Useful for "load more" patterns in UI where offset pagination is inefficient
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getUpcomingAppointmentsCursor(LocalDateTime afterDate, Pageable pageable) {
        log.info("Fetching upcoming appointments after: {}", afterDate);
        
        return appointmentRepository.findUpcomingAppointmentsCursor(afterDate, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching appointments between {} and {}", startDate, endDate);
        
        return appointmentRepository.findAppointmentsBetweenDatesOptimized(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getCompletedAppointmentsWithPrescriptions() {
        log.info("Fetching completed appointments with prescriptions");
        
        return appointmentRepository.findAppointmentsWithPrescriptions()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Appointment updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }
        
        Appointment appointment = appointmentOpt.get();
        appointment.setReason(appointmentDTO.getReason());
        appointment.setNotes(appointmentDTO.getNotes());
        appointment.setDurationMinutes(appointmentDTO.getDurationMinutes());
        
        return appointmentRepository.save(appointment);
    }
    
    @Override
    public void cancelAppointment(Long id) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
        }
    }
    
    @Override
    public void completeAppointment(Long id) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
            appointmentRepository.save(appointment);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getAppointmentCountForPatient(Long patientId) {
        return appointmentRepository.countAppointmentsByPatientId(patientId);
    }
    
    /**
     * PERFORMANCE COMPARISON TEST
     * 
     * This method demonstrates the actual performance difference between:
     * - Naive queries (with N+1 problem)
     * - Optimized queries (with JOIN FETCH)
     * 
     * In a real scenario with hundreds or thousands of records,
     * the difference would be even more dramatic.
     */
    @Override
    @Transactional(readOnly = true)
    public PerformanceMetrics comparePerformance() {
        log.warn("===== PERFORMANCE COMPARISON TEST =====");
        
        // Test 1: Naive approach with N+1 problem
        log.warn("Starting NAIVE query test (with N+1 problem)...");
        long naiveStart = System.nanoTime();
        List<AppointmentDTO> naiveResults = getAllAppointmentsNaive();
        long naiveDuration = System.nanoTime() - naiveStart;
        long naiveTimeMs = naiveDuration / 1_000_000;
        
        log.warn("Naive results: {} appointments, Time: {} ms", naiveResults.size(), naiveTimeMs);
        
        // Test 2: Optimized approach with JOIN FETCH
        log.info("Starting OPTIMIZED query test (with JOIN FETCH)...");
        long optimizedStart = System.nanoTime();
        List<AppointmentDTO> optimizedResults = getAllAppointmentsOptimized();
        long optimizedDuration = System.nanoTime() - optimizedStart;
        long optimizedTimeMs = optimizedDuration / 1_000_000;
        
        log.info("Optimized results: {} appointments, Time: {} ms", optimizedResults.size(), optimizedTimeMs);
        
        // Create and populate metrics
        PerformanceMetrics metrics = PerformanceMetrics.builder()
                .naiveQueryTime(naiveTimeMs)
                .optimizedQueryTime(optimizedTimeMs)
                .recordsCount(optimizedResults.size())
                .build();
        
        metrics.calculateImprovement();
        
        log.warn("===== PERFORMANCE RESULTS =====");
        log.warn("Naive Approach: {} ms", metrics.getNaiveQueryTime());
        log.warn("Optimized Approach: {} ms", metrics.getOptimizedQueryTime());
        log.warn("Time Saved: {} ms ({}% improvement)", metrics.getTimeSaved(), 
                String.format("%.2f", metrics.getImprovementPercentage()));
        log.warn("Records Processed: {}", metrics.getRecordsCount());
        
        return metrics;
    }
    
    // Helper method: Convert Appointment entity to DTO
    private AppointmentDTO convertToDTO(Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .doctorId(appointment.getDoctor().getId())
                .appointmentDate(appointment.getAppointmentDate())
                .durationMinutes(appointment.getDurationMinutes())
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .status(appointment.getStatus().name())
                .prescriptionCount(appointment.getPrescriptions() != null ? 
                        appointment.getPrescriptions().size() : 0)
                .build();
    }
}
