-- ============================================================
-- Healthcare Management System — Seed Data
-- Run this AFTER the application has started and created tables
-- ============================================================

-- First, insert departments (if they don't exist)
INSERT INTO departments (id, name, description, head_of_department, contact_number, location, is_active, created_at, updated_at)
VALUES
  (1, 'Cardiology', 'Heart and cardiovascular care', 'Dr. Rajesh Kumar', '9876543210', 'Building A, Floor 2', true, NOW(), NOW()),
  (2, 'Neurology', 'Brain and nervous system care', 'Dr. Priya Sharma', '9876543211', 'Building A, Floor 3', true, NOW(), NOW()),
  (3, 'Orthopedics', 'Bone, joint and muscle care', 'Dr. Amit Patel', '9876543212', 'Building B, Floor 1', true, NOW(), NOW()),
  (4, 'Dermatology', 'Skin, hair and nail care', 'Dr. Sneha Gupta', '9876543213', 'Building B, Floor 2', true, NOW(), NOW()),
  (5, 'Pediatrics', 'Child healthcare', 'Dr. Vikram Singh', '9876543214', 'Building C, Floor 1', true, NOW(), NOW()),
  (6, 'General Medicine', 'Primary and internal medicine', 'Dr. Ananya Reddy', '9876543215', 'Building A, Floor 1', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Reset sequence to avoid conflicts
SELECT setval('departments_id_seq', (SELECT MAX(id) FROM departments));

-- ============================================================
-- PRE-CREATED DOCTOR ACCOUNTS
-- All passwords are: password123 (BCrypt encoded)
-- ============================================================
-- BCrypt hash for 'password123': $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- Generated with BCryptPasswordEncoder rounds=10

INSERT INTO doctors (id, first_name, last_name, license_number, specialization, qualification, contact_number, email, password, department_id, experience_years, consultation_fee, is_available, active, created_at, updated_at)
VALUES
  (1, 'Rajesh', 'Kumar', 'DOC-CARD-001', 'Interventional Cardiology', 'MBBS, MD, DM Cardiology', '9876543210', 'rajesh.kumar@hospital.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
   1, 18, 1500.00, true, true, NOW(), NOW()),

  (2, 'Priya', 'Sharma', 'DOC-NEUR-001', 'Clinical Neurology', 'MBBS, MD, DM Neurology', '9876543211', 'priya.sharma@hospital.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
   2, 14, 1200.00, true, true, NOW(), NOW()),

  (3, 'Amit', 'Patel', 'DOC-ORTH-001', 'Joint Replacement Surgery', 'MBBS, MS Orthopedics', '9876543212', 'amit.patel@hospital.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
   3, 12, 1000.00, true, true, NOW(), NOW()),

  (4, 'Sneha', 'Gupta', 'DOC-DERM-001', 'Cosmetic Dermatology', 'MBBS, MD Dermatology', '9876543213', 'sneha.gupta@hospital.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
   4, 9, 800.00, true, true, NOW(), NOW()),

  (5, 'Vikram', 'Singh', 'DOC-PEDI-001', 'Neonatal Pediatrics', 'MBBS, MD Pediatrics', '9876543214', 'vikram.singh@hospital.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
   5, 11, 700.00, true, true, NOW(), NOW()),

  (6, 'Ananya', 'Reddy', 'DOC-GENM-001', 'Internal Medicine', 'MBBS, MD General Medicine', '9876543215', 'ananya.reddy@hospital.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
   6, 8, 500.00, true, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Reset sequence to avoid conflicts on future inserts
SELECT setval('doctors_id_seq', (SELECT MAX(id) FROM doctors));

-- ============================================================
-- DOCTOR LOGIN CREDENTIALS REFERENCE
-- ============================================================
-- | Email                        | Password    | Specialization             |
-- |------------------------------|-------------|----------------------------|
-- | rajesh.kumar@hospital.com    | password123 | Interventional Cardiology  |
-- | priya.sharma@hospital.com    | password123 | Clinical Neurology         |
-- | amit.patel@hospital.com      | password123 | Joint Replacement Surgery  |
-- | sneha.gupta@hospital.com     | password123 | Cosmetic Dermatology       |
-- | vikram.singh@hospital.com    | password123 | Neonatal Pediatrics        |
-- | ananya.reddy@hospital.com    | password123 | Internal Medicine          |
-- ============================================================
