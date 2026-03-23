package com.example.demo.repository;

import com.example.demo.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DoctorRepository - Demonstrates advanced queries for Doctor entity
 * Includes filtering by specialization and performance metrics
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    Optional<Doctor> findByEmail(String email);
    
    /**
     * Find doctors by specialization with pagination
     * Useful for filtering doctors by expertise
     */
    Page<Doctor> findBySpecialization(String specialization, Pageable pageable);
    
    /**
     * JPQL Query: Find available doctors (with fewer appointments)
     * Demonstrates joining and aggregation
     * Helps identify doctors with less workload
     */
    @Query("SELECT d, COUNT(a) as appointmentCount FROM Doctor d " +
           "LEFT JOIN d.appointments a " +
           "GROUP BY d.id HAVING COUNT(a) < :maxAppointments " +
           "ORDER BY appointmentCount ASC")
    List<Object[]> findAvailableDoctors(@Param("maxAppointments") long maxAppointments);
    
    /**
     * JPQL Query: Find doctors by experience range
     * Useful for filtering doctors by expertise level
     */
    @Query("SELECT d FROM Doctor d WHERE d.yearsOfExperience >= :minYears AND d.yearsOfExperience <= :maxYears " +
           "ORDER BY d.yearsOfExperience DESC")
    Page<Doctor> findDoctorsByExperience(
            @Param("minYears") Integer minYears,
            @Param("maxYears") Integer maxYears,
            Pageable pageable);
    
    /**
     * JPQL Query: Find doctors by specialization and experience
     * Combined filtering for specific requirements
     */
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization " +
           "AND d.yearsOfExperience >= :minimumExperience " +
           "ORDER BY d.yearsOfExperience DESC")
    Page<Doctor> findDoctorsBySpecializationAndExperience(
            @Param("specialization") String specialization,
            @Param("minimumExperience") Integer minimumExperience,
            Pageable pageable);
    
    /**
     * JPQL Query: Find most experienced doctors
     * Useful for identifying senior practitioners
     */
    @Query("SELECT d FROM Doctor d ORDER BY d.yearsOfExperience DESC")
    List<Doctor> findMostExperiencedDoctors(Pageable pageable);
    
    /**
     * JPQL Query: Find doctors with highest appointment load
     * Demonstrates performance data collection
     */
    @Query("SELECT d FROM Doctor d WHERE SIZE(d.appointments) > :minAppointments " +
           "ORDER BY SIZE(d.appointments) DESC")
    List<Doctor> findDoctorsWithHighLoadByAppointmentCount(@Param("minAppointments") int minAppointments);
}
