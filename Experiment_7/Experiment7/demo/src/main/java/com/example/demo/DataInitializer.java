package com.example.demo;

import com.example.demo.dto.AppointmentDTO;
import com.example.demo.dto.DoctorDTO;
import com.example.demo.dto.PatientDTO;
import com.example.demo.dto.PrescriptionDTO;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import com.example.demo.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DataInitializer - Initializes sample data for testing
 * This allows testing the API without manual data creation
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("========== Initializing HealthHub Sample Data ==========");
        initializeSampleData();
        log.info("========== Sample Data Initialization Complete ==========");
    }
    
    private void initializeSampleData() {
        // Create sample patients
        log.info("Creating sample patients...");
        PatientDTO patient1 = PatientDTO.builder()
                .firstName("Raj")
                .lastName("Kumar")
                .email("raj.kumar@gmail.com")
                .phoneNumber("9876543210")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .medicalHistory("Hypertension, Diabetes")
                .address("123 Main St, City")
                .build();
        
        PatientDTO patient2 = PatientDTO.builder()
                .firstName("Priya")
                .lastName("Sharma")
                .email("priya.sharma@gmail.com")
                .phoneNumber("9876543211")
                .dateOfBirth(LocalDate.of(1992, 8, 20))
                .medicalHistory("Thyroid")
                .address("456 Oak Ave, City")
                .build();
        
        PatientDTO patient3 = PatientDTO.builder()
                .firstName("Amit")
                .lastName("Singh")
                .email("amit.singh@gmail.com")
                .phoneNumber("9876543212")
                .dateOfBirth(LocalDate.of(1988, 3, 10))
                .medicalHistory("No known allergies")
                .address("789 Pine Rd, City")
                .build();
        
        var createdPatient1 = patientService.createPatient(patient1);
        var createdPatient2 = patientService.createPatient(patient2);
        var createdPatient3 = patientService.createPatient(patient3);
        
        // Create sample doctors
        log.info("Creating sample doctors...");
        DoctorDTO doctor1 = DoctorDTO.builder()
                .firstName("Dr. Neha")
                .lastName("Verma")
                .email("dr.neha.verma@hospital.com")
                .phoneNumber("9876543301")
                .specialization("Cardiology")
                .licenseNumber("LIC001")
                .yearsOfExperience(10)
                .clinic("Heart Care Clinic")
                .build();
        
        DoctorDTO doctor2 = DoctorDTO.builder()
                .firstName("Dr. Rahul")
                .lastName("Patel")
                .email("dr.rahul.patel@hospital.com")
                .phoneNumber("9876543302")
                .specialization("Neurology")
                .licenseNumber("LIC002")
                .yearsOfExperience(8)
                .clinic("Brain & Spine Center")
                .build();
        
        DoctorDTO doctor3 = DoctorDTO.builder()
                .firstName("Dr. Anjali")
                .lastName("Desai")
                .email("dr.anjali.desai@hospital.com")
                .phoneNumber("9876543303")
                .specialization("Cardiology")
                .licenseNumber("LIC003")
                .yearsOfExperience(15)
                .clinic("Heart Care Clinic")
                .build();
        
        var createdDoctor1 = doctorService.createDoctor(doctor1);
        var createdDoctor2 = doctorService.createDoctor(doctor2);
        var createdDoctor3 = doctorService.createDoctor(doctor3);
        
        // Create sample appointments
        log.info("Creating sample appointments...");
        AppointmentDTO appointment1 = AppointmentDTO.builder()
                .patientId(createdPatient1.getId())
                .doctorId(createdDoctor1.getId())
                .appointmentDate(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0))
                .durationMinutes(30)
                .reason("Routine checkup")
                .status("SCHEDULED")
                .build();
        
        AppointmentDTO appointment2 = AppointmentDTO.builder()
                .patientId(createdPatient2.getId())
                .doctorId(createdDoctor2.getId())
                .appointmentDate(LocalDateTime.now().plusDays(2).withHour(14).withMinute(30))
                .durationMinutes(45)
                .reason("Headache and dizziness")
                .status("SCHEDULED")
                .build();
        
        AppointmentDTO appointment3 = AppointmentDTO.builder()
                .patientId(createdPatient3.getId())
                .doctorId(createdDoctor1.getId())
                .appointmentDate(LocalDateTime.now().minusDays(1).withHour(11).withMinute(0))
                .durationMinutes(30)
                .reason("Follow-up consultation")
                .status("COMPLETED")
                .build();
        
        var createdAppointment1 = appointmentService.createAppointment(appointment1);
        var createdAppointment2 = appointmentService.createAppointment(appointment2);
        var createdAppointment3 = appointmentService.createAppointment(appointment3);
        
        // Create sample prescriptions
        log.info("Creating sample prescriptions...");
        PrescriptionDTO prescription1 = PrescriptionDTO.builder()
                .patientId(createdPatient1.getId())
                .appointmentId(createdAppointment1.getId())
                .medicationName("Lisinopril")
                .dosage("10mg")
                .frequency("Once daily")
                .durationDays(30)
                .instructions("Take with water after food")
                .build();
        
        PrescriptionDTO prescription2 = PrescriptionDTO.builder()
                .patientId(createdPatient2.getId())
                .appointmentId(createdAppointment2.getId())
                .medicationName("Aspirin")
                .dosage("500mg")
                .frequency("Twice daily")
                .durationDays(7)
                .instructions("Take with meals")
                .build();
        
        PrescriptionDTO prescription3 = PrescriptionDTO.builder()
                .patientId(createdPatient3.getId())
                .appointmentId(createdAppointment3.getId())
                .medicationName("Vitamin C")
                .dosage("1000mg")
                .frequency("Once daily")
                .durationDays(60)
                .instructions("Take after breakfast")
                .build();
        
        prescriptionService.createPrescription(prescription1);
        prescriptionService.createPrescription(prescription2);
        prescriptionService.createPrescription(prescription3);
        
        log.info("✓ Created 3 patients");
        log.info("✓ Created 3 doctors");
        log.info("✓ Created 3 appointments");
        log.info("✓ Created 3 prescriptions");
    }
}
