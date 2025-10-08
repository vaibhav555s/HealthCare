package com.hospital.hospitalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.hospitalapi.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
