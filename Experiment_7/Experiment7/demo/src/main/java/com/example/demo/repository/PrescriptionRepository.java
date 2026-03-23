package com.example.demo.repository;

import com.example.demo.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * PrescriptionRepository - Queries for medication records
 * Demonstrates temporal queries and advanced filtering
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    
    /**
     * Find prescriptions for a patient with pagination
     */
    @Query("SELECT ps FROM Prescription ps WHERE ps.patient.id = :patientId " +
           "ORDER BY ps.prescribedDate DESC")
    Page<Prescription> findByPatientId(@Param("patientId") Long patientId, Pageable pageable);
    
    /**
     * Find active prescriptions (not expired)
     * Demonstrates temporal filtering
     */
    @Query("SELECT ps FROM Prescription ps WHERE ps.expiryDate > CURRENT_DATE " +
           "ORDER BY ps.expiryDate ASC")
    Page<Prescription> findActivePrescriptions(Pageable pageable);
    
    /**
     * Find expired prescriptions
     */
    @Query("SELECT ps FROM Prescription ps WHERE ps.expiryDate < CURRENT_DATE " +
           "ORDER BY ps.expiryDate DESC")
    Page<Prescription> findExpiredPrescriptions(Pageable pageable);
    
    /**
     * Find prescriptions by medication name
     */
    @Query("SELECT ps FROM Prescription ps WHERE LOWER(ps.medicationName) LIKE LOWER(CONCAT('%', :medicationName, '%')) " +
           "ORDER BY ps.prescribedDate DESC")
    Page<Prescription> findByMedicationName(
            @Param("medicationName") String medicationName,
            Pageable pageable);
    
    /**
     * Find prescriptions issued between dates
     */
    @Query("SELECT ps FROM Prescription ps WHERE ps.prescribedDate BETWEEN :startDate AND :endDate " +
           "ORDER BY ps.prescribedDate DESC")
    List<Prescription> findPrescriptionsBeteenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find patients' prescription history with prescriptions metadata
     * Useful for medication history reports
     */
    @Query("SELECT ps FROM Prescription ps " +
           "WHERE ps.patient.id = :patientId " +
           "ORDER BY ps.prescribedDate DESC")
    List<Prescription> findPatientPrescriptionHistory(@Param("patientId") Long patientId);
}
