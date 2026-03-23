package com.example.demo.service;

import com.example.demo.dto.AppointmentDTO;
import com.example.demo.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * AppointmentService - Interface demonstrating N+1 problem and its solutions
 */
public interface AppointmentService {
    
    Appointment createAppointment(AppointmentDTO appointmentDTO);
    
    Optional<Appointment> getAppointmentById(Long id);
    
    /**
     * ⚠️ PROBLEMATIC - Demonstrates N+1 problem
     * Each appointment fetch will trigger separate queries for patient and doctor
     */
    List<AppointmentDTO> getAllAppointmentsNaive();
    
    /**
     * ✅ OPTIMIZED - Solves N+1 using JOIN FETCH
     * Single query loads all appointments with their patient and doctor info
     */
    List<AppointmentDTO> getAllAppointmentsOptimized();
    
    /**
     * Find appointments for a patient with pagination
     */
    Page<AppointmentDTO> getAppointmentsByPatientId(Long patientId, Pageable pageable);
    
    /**
     * Find appointments for a doctor with pagination
     */
    Page<AppointmentDTO> getAppointmentsByDoctorId(Long doctorId, Pageable pageable);
    
    /**
     * Get upcoming appointments with cursor-based pagination
     * Cursor = the timestamp of the last appointment seen by client
     */
    Page<AppointmentDTO> getUpcomingAppointmentsCursor(LocalDateTime afterDate, Pageable pageable);
    
    /**
     * Get appointments between dates with full optimization
     * Includes all related data in single query
     */
    List<AppointmentDTO> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get completed appointments with prescriptions
     * Demonstrates multi-level relationships
     */
    List<AppointmentDTO> getCompletedAppointmentsWithPrescriptions();
    
    Appointment updateAppointment(Long id, AppointmentDTO appointmentDTO);
    
    void cancelAppointment(Long id);
    
    void completeAppointment(Long id);
    
    // Performance analytics
    long getAppointmentCountForPatient(Long patientId);
    
    /**
     * Performance test: Compare naive vs optimized queries
     * Returns execution times for both approaches
     */
    PerformanceMetrics comparePerformance();
}
