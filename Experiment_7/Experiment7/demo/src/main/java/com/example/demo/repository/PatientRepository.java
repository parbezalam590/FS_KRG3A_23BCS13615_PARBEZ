package com.example.demo.repository;

import com.example.demo.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PatientRepository - Advanced JPA queries demonstrating JPQL, pagination, and filtering
 * Includes examples of:
 * - Custom JPQL queries
 * - Pagination and cursor-based pagination
 * - Named parameters
 * - Sorting optimization
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Basic finding methods
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByPhoneNumber(String phoneNumber);
    
    /**
     * Find patients by first and last name
     * Standard pagination example
     */
    Page<Patient> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(
            String firstName, 
            String lastName, 
            Pageable pageable);
    
    /**
     * JPQL Query: Find all patients created after a specific date
     * Demonstrates explicit JPQL with date comparison
     */
    @Query("SELECT p FROM Patient p WHERE p.createdAt >= :createdAfter ORDER BY p.createdAt DESC")
    Page<Patient> findPatientsCreatedAfter(
            @Param("createdAfter") LocalDate createdAfter,
            Pageable pageable);
    
    /**
     * JPQL Query: Find patients with medical history
     * Demonstrates filtering by non-null fields
     */
    @Query("SELECT p FROM Patient p WHERE p.medicalHistory IS NOT NULL AND p.medicalHistory <> '' ORDER BY p.id")
    Page<Patient> findPatientsWithMedicalHistory(Pageable pageable);
    
    /**
     * JPQL Query: Search patients by name (partial match)
     * Demonstrates LIKE operator and parameter binding
     */
    @Query("SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY p.firstName")
    Page<Patient> searchPatientsByName(
            @Param("searchTerm") String searchTerm,
            Pageable pageable);
    
    /**
     * JPQL Query: Find patients by appointment count
     * Demonstrates aggregation and filtering
     * Useful for identifying frequently visiting patients
     */
    @Query("SELECT p FROM Patient p WHERE SIZE(p.appointments) > :minAppointments ORDER BY SIZE(p.appointments) DESC")
    List<Patient> findPatientsByMinimumAppointments(@Param("minAppointments") int minAppointments);
    
    /**
     * JPQL Query: Find active patients (with appointments after today)
     * Demonstrates JOIN and temporal filtering
     */
    @Query("SELECT DISTINCT p FROM Patient p " +
           "JOIN p.appointments a WHERE a.appointmentDate > CURRENT_TIMESTAMP " +
           "ORDER BY p.id")
    Page<Patient> findActivePatients(Pageable pageable);
    
    /**
     * JPQL Query: Find patients by age range
     * Demonstrates calculated field queries
     */
    @Query("SELECT p FROM Patient p WHERE YEAR(CURRENT_DATE) - YEAR(p.dateOfBirth) BETWEEN :minAge AND :maxAge " +
           "ORDER BY p.dateOfBirth DESC")
    List<Patient> findPatientsByAgeRange(
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge);
    
    /**
     * Native Query for advanced performance optimization
     * Returns only needed columns instead of full entity
     * Useful for read-only operations where full mapping is unnecessary
     */
    @Query(value = "SELECT id, first_name, last_name, email FROM patient LIMIT :limit OFFSET :offset", 
           nativeQuery = true)
    List<Object[]> findPatientSummaryPaginated(
            @Param("limit") int limit,
            @Param("offset") int offset);
    
    /**
     * JPQL Query: Find patients created within a date range
     * Good for cursor-based pagination approach
     */
    @Query("SELECT p FROM Patient p WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate " +
           "ORDER BY p.createdAt, p.id")
    List<Patient> findPatientsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
