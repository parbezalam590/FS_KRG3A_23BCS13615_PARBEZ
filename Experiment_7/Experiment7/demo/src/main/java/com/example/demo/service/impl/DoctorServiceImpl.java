package com.example.demo.service.impl;

import com.example.demo.dto.DoctorDTO;
import com.example.demo.entity.Doctor;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DoctorServiceImpl - Implementation of doctor operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DoctorServiceImpl implements DoctorService {
    
    private final DoctorRepository doctorRepository;
    
    @Override
    public Doctor createDoctor(DoctorDTO doctorDTO) {
        log.info("Creating new doctor: {} {}", doctorDTO.getFirstName(), doctorDTO.getLastName());
        
        Doctor doctor = Doctor.builder()
                .firstName(doctorDTO.getFirstName())
                .lastName(doctorDTO.getLastName())
                .email(doctorDTO.getEmail())
                .phoneNumber(doctorDTO.getPhoneNumber())
                .specialization(doctorDTO.getSpecialization())
                .licenseNumber(doctorDTO.getLicenseNumber())
                .yearsOfExperience(doctorDTO.getYearsOfExperience())
                .clinic(doctorDTO.getClinic())
                .build();
        
        return doctorRepository.save(doctor);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Doctor> getDoctorById(Long id) {
        log.debug("Fetching doctor by ID: {}", id);
        return doctorRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Doctor> getDoctorByEmail(String email) {
        log.debug("Fetching doctor by email: {}", email);
        return doctorRepository.findByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        log.info("Fetching all doctors");
        return doctorRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Doctor> getDoctorsBySpecialization(String specialization, Pageable pageable) {
        log.info("Fetching doctors with specialization: {}", specialization);
        return doctorRepository.findBySpecialization(specialization, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DoctorDTO> getAvailableDoctors(long maxAppointments) {
        log.info("Fetching available doctors with max {} appointments", maxAppointments);
        
        long startTime = System.currentTimeMillis();
        List<Object[]> results = doctorRepository.findAvailableDoctors(maxAppointments);
        long duration = System.currentTimeMillis() - startTime;
        
        log.info("Found {} available doctors in {} ms", results.size(), duration);
        
        return results.stream()
                .map(row -> {
                    Doctor doctor = (Doctor) row[0];
                    Long appointmentCount = (Long) row[1];
                    return DoctorDTO.builder()
                            .id(doctor.getId())
                            .firstName(doctor.getFirstName())
                            .lastName(doctor.getLastName())
                            .email(doctor.getEmail())
                            .phoneNumber(doctor.getPhoneNumber())
                            .specialization(doctor.getSpecialization())
                            .licenseNumber(doctor.getLicenseNumber())
                            .yearsOfExperience(doctor.getYearsOfExperience())
                            .clinic(doctor.getClinic())
                            .appointmentCount(appointmentCount)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Doctor> getDoctorsByExperience(Integer minYears, Integer maxYears, Pageable pageable) {
        log.info("Fetching doctors with experience between {} and {} years", minYears, maxYears);
        return doctorRepository.findDoctorsByExperience(minYears, maxYears, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Doctor> getDoctorsBySpecializationAndExperience(
            String specialization,
            Integer minimumExperience,
            Pageable pageable) {
        log.info("Fetching {} doctors with at least {} years experience", specialization, minimumExperience);
        return doctorRepository.findDoctorsBySpecializationAndExperience(specialization, minimumExperience, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Doctor> getMostExperiencedDoctors(Pageable pageable) {
        log.info("Fetching most experienced doctors");
        return doctorRepository.findMostExperiencedDoctors(pageable);
    }
    
    @Override
    public Doctor updateDoctor(Long id, DoctorDTO doctorDTO) {
        log.info("Updating doctor with ID: {}", id);
        
        Optional<Doctor> doctorOpt = doctorRepository.findById(id);
        if (doctorOpt.isEmpty()) {
            throw new RuntimeException("Doctor not found with ID: " + id);
        }
        
        Doctor doctor = doctorOpt.get();
        doctor.setFirstName(doctorDTO.getFirstName());
        doctor.setLastName(doctorDTO.getLastName());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setLicenseNumber(doctorDTO.getLicenseNumber());
        doctor.setYearsOfExperience(doctorDTO.getYearsOfExperience());
        doctor.setClinic(doctorDTO.getClinic());
        
        return doctorRepository.save(doctor);
    }
    
    @Override
    public void deleteDoctor(Long id) {
        log.info("Deleting doctor with ID: {}", id);
        doctorRepository.deleteById(id);
    }
}
