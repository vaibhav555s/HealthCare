package com.hospital.hospitalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.hospitalapi.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
