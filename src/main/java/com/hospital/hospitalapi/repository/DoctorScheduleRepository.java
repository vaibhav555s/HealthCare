package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.DoctorSchedule;
import com.hospital.hospitalapi.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByDoctor(Doctor doctor);
    List<DoctorSchedule> findByDoctorAndDayOfWeek(Doctor doctor, String dayOfWeek);
    List<DoctorSchedule> findByDoctorAndIsActive(Doctor doctor, Boolean isActive);
}