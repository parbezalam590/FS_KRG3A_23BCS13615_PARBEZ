package com.example.demo.repository;

import com.example.demo.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AppointmentRepository - Advanced queries demonstrating N+1 problem solutions
 * and performance optimization techniques
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    /**
     * ⚠️ PROBLEMATIC QUERY - Demonstrates N+1 Problem
     * This query will fetch all appointments, then for each appointment,
     * it will load the Patient and Doctor separately
     * Total queries = 1 (appointments) + N (patients) + N (doctors)
     */
    @Query("SELECT a FROM Appointment a WHERE a.status = :status")
    List<Appointment> findByStatusBasic(@Param("status") Appointment.AppointmentStatus status);
    
    /**
     * ✅ OPTIMIZED QUERY - Solves N+1 Problem using EAGER loading
     * Uses LEFT JOIN FETCH to load related entities in a single query
     * This is one way to solve the N+1 problem
     */
    @Query("SELECT DISTINCT a FROM Appointment a " +
           "LEFT JOIN FETCH a.patient " +
           "LEFT JOIN FETCH a.doctor " +
           "WHERE a.status = :status")
    List<Appointment> findByStatusOptimized(@Param("status") Appointment.AppointmentStatus status);
    
    /**
     * Find appointments by patient with pagination
     * Returns page of appointments for a specific patient
     */
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId " +
           "ORDER BY a.appointmentDate DESC")
    Page<Appointment> findByPatientId(@Param("patientId") Long patientId, Pageable pageable);
    
    /**
     * Find appointments by doctor with pagination
     */
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "ORDER BY a.appointmentDate DESC")
    Page<Appointment> findByDoctorId(@Param("doctorId") Long doctorId, Pageable pageable);
    
    /**
     * Find upcoming appointments - cursor-based pagination approach
     * Useful for real-time updates without skipping records
     * Client provides the last timestamp and continues from there
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate > :afterDate " +
           "ORDER BY a.appointmentDate ASC")
    Page<Appointment> findUpcomingAppointmentsCursor(
            @Param("afterDate") LocalDateTime afterDate,
            Pageable pageable);
    
    /**
     * Find appointments in a date range - optimized with JOINs
     * Demonstrates combining multiple conditions with proper fetching
     */
    @Query("SELECT DISTINCT a FROM Appointment a " +
           "LEFT JOIN FETCH a.patient p " +
           "LEFT JOIN FETCH a.doctor d " +
           "WHERE a.appointmentDate BETWEEN :startDate AND :endDate " +
           "ORDER BY a.appointmentDate")
    List<Appointment> findAppointmentsBetweenDatesOptimized(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Count appointments for a specific patient
     * Useful for checking appointment frequency
     */
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.patient.id = :patientId")
    Long countAppointmentsByPatientId(@Param("patientId") Long patientId);
    
    /**
     * Find completed appointments between dates
     */
    @Query("SELECT a FROM Appointment a " +
           "WHERE a.appointmentDate BETWEEN :startDate AND :endDate " +
           "AND a.status = 'COMPLETED' " +
           "ORDER BY a.appointmentDate DESC")
    Page<Appointment> findCompletedAppointmentsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
    
    /**
     * Find appointments with prescriptions
     * Double JOIN to get full prescription data
     */
    @Query("SELECT DISTINCT a FROM Appointment a " +
           "LEFT JOIN FETCH a.prescriptions pr " +
           "LEFT JOIN FETCH a.patient " +
           "LEFT JOIN FETCH a.doctor " +
           "WHERE a.status = 'COMPLETED' AND SIZE(a.prescriptions) > 0")
    List<Appointment> findAppointmentsWithPrescriptions();
}
