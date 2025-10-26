package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Appointment;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByTokenNumber(String tokenNumber);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByAppointmentDate(LocalDate date);
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate date);
    List<Appointment> findByDepartmentAndAppointmentDate(Department department, LocalDate date);
    List<Appointment> findByStatus(String status);
}
