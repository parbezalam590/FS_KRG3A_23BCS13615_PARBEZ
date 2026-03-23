package com.example.demo.controller;

import com.example.demo.dto.AppointmentDTO;
import com.example.demo.entity.Appointment;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.PerformanceMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * AppointmentController - REST API for appointment management
 * Key Feature: Demonstrates N+1 problem and its solutions
 */
@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.status(201).body(appointmentService.createAppointment(appointmentDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.isPresent() ? 
                ResponseEntity.ok(appointment.get()) : 
                ResponseEntity.notFound().build();
    }
    
    /**
     * ⚠️ PROBLEMATIC ENDPOINT - Demonstrates N+1 Problem
     * This endpoint will execute:
     * 1 query to get all appointments
     * + N queries to load each patient
     * + N queries to load each doctor
     * = 2N + 1 total queries
     * 
     * Usage: GET /api/v1/appointments/naive-all
     * 
     * Watch the logs to see the performance impact!
     */
    @GetMapping("/naive-all")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsNaive() {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsNaive());
    }
    
    /**
     * ✅ OPTIMIZED ENDPOINT - Solves N+1 Problem using JOIN FETCH
     * This endpoint uses a single query with JOINs to load all data
     * Much more efficient than the naive approach above!
     * 
     * Usage: GET /api/v1/appointments/optimized-all
     * 
     * Compare the logs between /naive-all and /optimized-all to see the difference!
     */
    @GetMapping("/optimized-all")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsOptimized() {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsOptimized());
    }
    
    /**
     * Find appointments for a specific patient
     * Usage: GET /api/v1/appointments/patient/1?page=0&size=10
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByPatient(
            @PathVariable Long patientId,
            Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId, pageable));
    }
    
    /**
     * Find appointments for a specific doctor
     * Usage: GET /api/v1/appointments/doctor/1?page=0&size=10
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId, pageable));
    }
    
    /**
     * Cursor-based pagination for upcoming appointments
     * Usage: GET /api/v1/appointments/upcoming?after=2024-01-01T10:00:00&page=0&size=20
     */
    @GetMapping("/upcoming")
    public ResponseEntity<Page<AppointmentDTO>> getUpcomingAppointments(
            @RequestParam String after,
            Pageable pageable) {
        LocalDateTime afterDate = LocalDateTime.parse(after);
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsCursor(afterDate, pageable));
    }
    
    /**
     * Get appointments between two dates
     */
    @GetMapping("/between")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsBetweenDates(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.ok(appointmentService.getAppointmentsBetweenDates(
                LocalDateTime.parse(startDate),
                LocalDateTime.parse(endDate)));
    }
    
    /**
     * Get completed appointments with prescriptions
     */
    @GetMapping("/completed-with-prescriptions")
    public ResponseEntity<List<AppointmentDTO>> getCompletedAppointmentsWithPrescriptions() {
        return ResponseEntity.ok(appointmentService.getCompletedAppointmentsWithPrescriptions());
    }
    
    /**
     * PERFORMANCE TEST ENDPOINT
     * 
     * This endpoint runs a performance comparison between:
     * - Naive queries (N+1 problem)
     * - Optimized queries (JOIN FETCH)
     * 
     * Returns metrics showing the performance difference!
     * 
     * Usage: GET /api/v1/appointments/performance-test
     * 
     * This will execute both queries and show you exactly how much better the optimized approach is!
     */
    @GetMapping("/performance-test")
    public ResponseEntity<PerformanceMetrics> performanceTest() {
        return ResponseEntity.ok(appointmentService.comparePerformance());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentDTO appointmentDTO) {
        try {
            return ResponseEntity.ok(appointmentService.updateAppointment(id, appointmentDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(new Object() { 
            public String message = "Appointment cancelled"; 
        });
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeAppointment(@PathVariable Long id) {
        appointmentService.completeAppointment(id);
        return ResponseEntity.ok(new Object() { 
            public String message = "Appointment completed"; 
        });
    }
}
