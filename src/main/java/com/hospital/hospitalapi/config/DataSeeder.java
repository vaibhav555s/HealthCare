package com.hospital.hospitalapi.config;

import com.hospital.hospitalapi.entity.Department;
import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.repository.DepartmentRepository;
import com.hospital.hospitalapi.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Only seed if no doctors exist
        if (doctorRepository.count() > 0) {
            log.info("Doctors already exist, skipping seed data.");
            return;
        }

        log.info("Seeding departments and doctors...");

        // Create departments
        Department cardiology = createDepartment("Cardiology", "Heart and cardiovascular care", "Dr. Rajesh Kumar", "Building A, Floor 2");
        Department neurology = createDepartment("Neurology", "Brain and nervous system care", "Dr. Priya Sharma", "Building A, Floor 3");
        Department orthopedics = createDepartment("Orthopedics", "Bone, joint and muscle care", "Dr. Amit Patel", "Building B, Floor 1");
        Department dermatology = createDepartment("Dermatology", "Skin, hair and nail care", "Dr. Sneha Gupta", "Building B, Floor 2");
        Department pediatrics = createDepartment("Pediatrics", "Child healthcare", "Dr. Vikram Singh", "Building C, Floor 1");
        Department generalMedicine = createDepartment("General Medicine", "Primary and internal medicine", "Dr. Ananya Reddy", "Building A, Floor 1");

        String encodedPassword = passwordEncoder.encode("password123");

        // Create doctors
        createDoctor("Rajesh", "Kumar", "DOC-CARD-001", "Interventional Cardiology",
                "MBBS, MD, DM Cardiology", "9876543210", "rajesh.kumar@hospital.com",
                encodedPassword, cardiology, 18, 1500.00);

        createDoctor("Priya", "Sharma", "DOC-NEUR-001", "Clinical Neurology",
                "MBBS, MD, DM Neurology", "9876543211", "priya.sharma@hospital.com",
                encodedPassword, neurology, 14, 1200.00);

        createDoctor("Amit", "Patel", "DOC-ORTH-001", "Joint Replacement Surgery",
                "MBBS, MS Orthopedics", "9876543212", "amit.patel@hospital.com",
                encodedPassword, orthopedics, 12, 1000.00);

        createDoctor("Sneha", "Gupta", "DOC-DERM-001", "Cosmetic Dermatology",
                "MBBS, MD Dermatology", "9876543213", "sneha.gupta@hospital.com",
                encodedPassword, dermatology, 9, 800.00);

        createDoctor("Vikram", "Singh", "DOC-PEDI-001", "Neonatal Pediatrics",
                "MBBS, MD Pediatrics", "9876543214", "vikram.singh@hospital.com",
                encodedPassword, pediatrics, 11, 700.00);

        createDoctor("Ananya", "Reddy", "DOC-GENM-001", "Internal Medicine",
                "MBBS, MD General Medicine", "9876543215", "ananya.reddy@hospital.com",
                encodedPassword, generalMedicine, 8, 500.00);

        log.info("✅ Seeded 6 departments and 6 doctors. All doctor passwords: password123");
    }

    private Department createDepartment(String name, String description, String head, String location) {
        Department existing = departmentRepository.findByName(name).orElse(null);
        if (existing != null) return existing;

        Department dept = new Department();
        dept.setName(name);
        dept.setDescription(description);
        dept.setHeadOfDepartment(head);
        dept.setContactNumber("0000000000");
        dept.setLocation(location);
        dept.setIsActive(true);
        return departmentRepository.save(dept);
    }

    private void createDoctor(String firstName, String lastName, String license,
                              String specialization, String qualification,
                              String contactNumber, String email, String password,
                              Department department, int experience, double fee) {
        if (doctorRepository.findByEmail(email).isPresent()) return;

        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setLicenseNumber(license);
        doctor.setSpecialization(specialization);
        doctor.setQualification(qualification);
        doctor.setContactNumber(contactNumber);
        doctor.setEmail(email);
        doctor.setPassword(password);
        doctor.setDepartment(department);
        doctor.setExperienceYears(experience);
        doctor.setConsultationFee(fee);
        doctor.setIsAvailable(true);
        doctor.setIsActive(true);
        doctorRepository.save(doctor);
    }
}
