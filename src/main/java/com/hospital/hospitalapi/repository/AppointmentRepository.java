package com.hospital.hospitalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.hospitalapi.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
