package com.example.demo.service;

import com.example.demo.dto.DoctorDTO;
import com.example.demo.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * DoctorService - Interface for doctor operations
 */
public interface DoctorService {
    
    Doctor createDoctor(DoctorDTO doctorDTO);
    
    Optional<Doctor> getDoctorById(Long id);
    
    Optional<Doctor> getDoctorByEmail(String email);
    
    Page<Doctor> getAllDoctors(Pageable pageable);
    
    /**
     * Find doctors by specialization
     */
    Page<Doctor> getDoctorsBySpecialization(String specialization, Pageable pageable);
    
    /**
     * Find available doctors (with fewer appointments)
     * Filters doctors by workload
     */
    List<DoctorDTO> getAvailableDoctors(long maxAppointments);
    
    /**
     * Find doctors by experience
     */
    Page<Doctor> getDoctorsByExperience(Integer minYears, Integer maxYears, Pageable pageable);
    
    /**
     * Find doctors by specialization and experience
     */
    Page<Doctor> getDoctorsBySpecializationAndExperience(
            String specialization,
            Integer minimumExperience,
            Pageable pageable);
    
    List<Doctor> getMostExperiencedDoctors(Pageable pageable);
    
    Doctor updateDoctor(Long id, DoctorDTO doctorDTO);
    
    void deleteDoctor(Long id);
}
