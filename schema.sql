-- =====================================================================
-- Hospital Management System (HMS) - MySQL Database Schema & Initial Data
-- =====================================================================

CREATE DATABASE IF NOT EXISTS hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hospital_db;

-- 1. Users Table (Authentication)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'ADMIN'
) ENGINE=InnoDB;

-- 2. Doctors Table
CREATE TABLE IF NOT EXISTS doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    age DOUBLE NOT NULL,
    specialization VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- 3. Patients Table
CREATE TABLE IF NOT EXISTS patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    age DOUBLE NOT NULL,
    ailment VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- 4. Appointments Table
CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    date VARCHAR(50) NOT NULL,
    time VARCHAR(50) NOT NULL,
    CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Bills Table
CREATE TABLE IF NOT EXISTS bills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_bill_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6. Follow-Ups Table
CREATE TABLE IF NOT EXISTS followups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    next_date VARCHAR(50) NOT NULL,
    notes TEXT,
    CONSTRAINT fk_followup_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 7. Medicines Table
CREATE TABLE IF NOT EXISTS medicines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    price DOUBLE NOT NULL
) ENGINE=InnoDB;

-- 8. Prescriptions Table
CREATE TABLE IF NOT EXISTS prescriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    medicine_details VARCHAR(255) NOT NULL,
    dosage VARCHAR(255) NOT NULL,
    CONSTRAINT fk_prescription_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- INITIAL SEED DATA (Legacy Migration Baseline)
-- =====================================================================

-- =====================================================

-- APOLLO CLINIC PORTAL

-- =====================================================

-- COMPLETE FRESH DATA SEEDING

-- Users: 2

-- All other tables: 20 records each

-- =====================================================

-- 1. CLEAR EXISTING DATA

-- Backup your database before running this script.
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM prescriptions;
DELETE FROM followups;
DELETE FROM bills;
DELETE FROM appointments;
DELETE FROM medicines;
DELETE FROM patients;
DELETE FROM doctors;
DELETE FROM users;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================

-- 2. DOCTORS: 20 RECORDS

-- Primary keys: 1001-1020

-- =====================================================

INSERT INTO doctors (id, name, gender, age, specialization, contact) VALUES
    (1001, 'Dr. Aarav Sharma', 'MALE', 45, 'Cardiology', '9810001001'),
    (1002, 'Dr. Ananya Verma', 'FEMALE', 38, 'Dermatology', '9810001002'),
    (1003, 'Dr. Rohan Mehta', 'MALE', 42, 'Orthopedics', '9810001003'),
    (1004, 'Dr. Priya Nair', 'FEMALE', 36, 'Pediatrics', '9810001004'),
    (1005, 'Dr. Arjun Kulkarni', 'MALE', 48, 'General Medicine', '9810001005'),
    (1006, 'Dr. Sneha Iyer', 'FEMALE', 34, 'ENT', '9810001006'),
    (1007, 'Dr. Faizan Ahmed', 'MALE', 44, 'Neurology', '9810001007'),
    (1008, 'Dr. Meera Chatterjee', 'FEMALE', 41, 'Gynecology', '9810001008'),
    (1009, 'Dr. Karan Malhotra', 'MALE', 39, 'Pulmonology', '9810001009'),
    (1010, 'Dr. Ishita Bose', 'FEMALE', 37, 'Ophthalmology', '9810001010'),
    (1011, 'Dr. Vivek Sinha', 'MALE', 46, 'Gastroenterology', '9810001011'),
    (1012, 'Dr. Ritu Kapoor', 'FEMALE', 40, 'Endocrinology', '9810001012'),
    (1013, 'Dr. Nikhil Menon', 'MALE', 43, 'Urology', '9810001013'),
    (1014, 'Dr. Kavita Rao', 'FEMALE', 35, 'Psychiatry', '9810001014'),
    (1015, 'Dr. Siddharth Joshi', 'MALE', 41, 'Nephrology', '9810001015'),
    (1016, 'Dr. Pooja Deshmukh', 'FEMALE', 38, 'Oncology', '9810001016'),
    (1017, 'Dr. Aman Gupta', 'MALE', 37, 'Radiology', '9810001017'),
    (1018, 'Dr. Shreya Das', 'FEMALE', 33, 'Rheumatology', '9810001018'),
    (1019, 'Dr. Aditya Bansal', 'MALE', 49, 'General Surgery', '9810001019'),
    (1020, 'Dr. Neha Srivastava', 'FEMALE', 36, 'Family Medicine', '9810001020');


-- =====================================================

-- 3. PATIENTS: 20 RECORDS

-- Primary keys: 2001-2020

-- =====================================================

INSERT INTO patients (id, name, gender, age, ailment) VALUES
    (2001, 'Rahul Mishra', 'MALE', 29, 'Seasonal allergic rhinitis'),
    (2002, 'Priyanka Singh', 'FEMALE', 34, 'Routine blood pressure review'),
    (2003, 'Amit Kumar', 'MALE', 51, 'Knee pain assessment'),
    (2004, 'Neha Gupta', 'FEMALE', 8, 'Routine pediatric consultation'),
    (2005, 'Mohammad Saif', 'MALE', 42, 'Acidity and indigestion'),
    (2006, 'Kavita Yadav', 'FEMALE', 56, 'Type 2 diabetes follow-up'),
    (2007, 'Rohit Verma', 'MALE', 25, 'Ear discomfort'),
    (2008, 'Sana Khan', 'FEMALE', 31, 'Routine gynecology consultation'),
    (2009, 'Vikas Patel', 'MALE', 47, 'Persistent cough assessment'),
    (2010, 'Anjali Roy', 'FEMALE', 40, 'Vision examination'),
    (2011, 'Deepak Tiwari', 'MALE', 63, 'Blood pressure monitoring'),
    (2012, 'Farah Siddiqui', 'FEMALE', 27, 'Migraine assessment'),
    (2013, 'Kunal Bhatia', 'MALE', 36, 'Digestive discomfort'),
    (2014, 'Ritika Das', 'FEMALE', 19, 'Seasonal allergy symptoms'),
    (2015, 'Suresh Prasad', 'MALE', 58, 'Joint pain consultation'),
    (2016, 'Pallavi Joshi', 'FEMALE', 45, 'Thyroid follow-up'),
    (2017, 'Imran Ali', 'MALE', 33, 'Urinary symptoms assessment'),
    (2018, 'Nandini Arora', 'FEMALE', 52, 'Routine health examination'),
    (2019, 'Manish Choudhary', 'MALE', 14, 'Respiratory symptom assessment'),
    (2020, 'Ayesha Qureshi', 'FEMALE', 30, 'Preventive health consultation');


-- =====================================================

-- 4. APPOINTMENTS: 20 RECORDS

-- Primary keys: 3001-3020

-- =====================================================

INSERT INTO appointments (id, patient_id, doctor_id, date, time) VALUES
    (3001, 2001, 1002, '2026-10-26', '09:00 AM'),
    (3002, 2002, 1005, '2026-10-26', '09:30 AM'),
    (3003, 2003, 1003, '2026-10-26', '10:00 AM'),
    (3004, 2004, 1004, '2026-10-26', '10:30 AM'),
    (3005, 2005, 1011, '2026-10-26', '11:00 AM'),
    (3006, 2006, 1012, '2026-10-27', '09:00 AM'),
    (3007, 2007, 1006, '2026-10-27', '09:30 AM'),
    (3008, 2008, 1008, '2026-10-27', '10:00 AM'),
    (3009, 2009, 1009, '2026-10-27', '10:30 AM'),
    (3010, 2010, 1010, '2026-10-27', '11:00 AM'),
    (3011, 2011, 1005, '2026-10-28', '09:00 AM'),
    (3012, 2012, 1007, '2026-10-28', '09:30 AM'),
    (3013, 2013, 1011, '2026-10-28', '10:00 AM'),
    (3014, 2014, 1002, '2026-10-28', '10:30 AM'),
    (3015, 2015, 1003, '2026-10-28', '11:00 AM'),
    (3016, 2016, 1012, '2026-10-29', '09:00 AM'),
    (3017, 2017, 1013, '2026-10-29', '09:30 AM'),
    (3018, 2018, 1020, '2026-10-29', '10:00 AM'),
    (3019, 2019, 1009, '2026-10-29', '10:30 AM'),
    (3020, 2020, 1014, '2026-10-29', '11:00 AM');


-- =====================================================

-- 5. BILLS: 20 RECORDS

-- Primary keys: 4001-4020

-- Statuses: PAID, UNPAID, OVERDUE

-- =====================================================

INSERT INTO bills (id, patient_id, amount, status) VALUES
    (4001, 2001, 650.00, 'PAID'),
    (4002, 2002, 500.00, 'UNPAID'),
    (4003, 2003, 1200.00, 'PAID'),
    (4004, 2004, 450.00, 'PAID'),
    (4005, 2005, 850.00, 'OVERDUE'),
    (4006, 2006, 700.00, 'PAID'),
    (4007, 2007, 550.00, 'UNPAID'),
    (4008, 2008, 900.00, 'PAID'),
    (4009, 2009, 750.00, 'OVERDUE'),
    (4010, 2010, 600.00, 'PAID'),
    (4011, 2011, 800.00, 'UNPAID'),
    (4012, 2012, 950.00, 'OVERDUE'),
    (4013, 2013, 700.00, 'PAID'),
    (4014, 2014, 500.00, 'PAID'),
    (4015, 2015, 1100.00, 'OVERDUE'),
    (4016, 2016, 650.00, 'PAID'),
    (4017, 2017, 850.00, 'UNPAID'),
    (4018, 2018, 750.00, 'PAID'),
    (4019, 2019, 550.00, 'OVERDUE'),
    (4020, 2020, 600.00, 'UNPAID');


-- =====================================================

-- 6. FOLLOW-UPS: 20 RECORDS

-- Primary keys: 5001-5020

-- =====================================================

INSERT INTO followups (id, patient_id, next_date, notes) VALUES
    (5001, 2001, '2026-11-05', 'Review allergy symptoms and treatment response.'),
    (5002, 2002, '2026-11-06', 'Review home blood pressure readings.'),
    (5003, 2003, '2026-11-06', 'Reassess knee mobility and pain symptoms.'),
    (5004, 2004, '2026-11-07', 'Routine pediatric follow-up with parent or guardian.'),
    (5005, 2005, '2026-11-08', 'Review digestive symptoms and dietary history.'),
    (5006, 2006, '2026-11-09', 'Review glucose monitoring records.'),
    (5007, 2007, '2026-11-09', 'Reassess ear discomfort and examination findings.'),
    (5008, 2008, '2026-11-10', 'Review consultation findings and care plan.'),
    (5009, 2009, '2026-11-10', 'Review cough symptoms and respiratory examination.'),
    (5010, 2010, '2026-11-11', 'Discuss vision examination results.'),
    (5011, 2011, '2026-11-12', 'Review blood pressure records and medication history.'),
    (5012, 2012, '2026-11-12', 'Review headache frequency and associated symptoms.'),
    (5013, 2013, '2026-11-13', 'Review digestive symptoms and clinical assessment.'),
    (5014, 2014, '2026-11-13', 'Assess allergy symptoms at the next visit.'),
    (5015, 2015, '2026-11-14', 'Review joint mobility and pain-related concerns.'),
    (5016, 2016, '2026-11-15', 'Review thyroid monitoring results.'),
    (5017, 2017, '2026-11-15', 'Review urinary symptoms and clinical findings.'),
    (5018, 2018, '2026-11-16', 'Review routine examination findings.'),
    (5019, 2019, '2026-11-17', 'Reassess respiratory symptoms with a guardian present.'),
    (5020, 2020, '2026-11-17', 'Review preventive health consultation findings.');


-- =====================================================

-- 7. MEDICINES: 20 RECORDS

-- Primary keys: 6001-6020

-- =====================================================

INSERT INTO medicines (id, name, stock, price) VALUES
    (6001, 'Paracetamol 500 mg', 120, 18.00),
    (6002, 'Cetirizine 10 mg', 80, 24.00),
    (6003, 'Omeprazole 20 mg', 65, 42.00),
    (6004, 'ORS Sachets', 150, 21.00),
    (6005, 'Calcium + Vitamin D Tablets', 90, 95.00),
    (6006, 'Saline Nasal Spray', 55, 78.00),
    (6007, 'Antacid Suspension', 70, 64.00),
    (6008, 'Multivitamin Tablets', 100, 110.00),
    (6009, 'Cough Relief Syrup', 45, 88.00),
    (6010, 'Lubricating Eye Drops', 50, 125.00),
    (6011, 'Metformin 500 mg', 90, 35.00),
    (6012, 'Amlodipine 5 mg', 75, 28.00),
    (6013, 'Pantoprazole 40 mg', 60, 48.00),
    (6014, 'Vitamin B-Complex Tablets', 85, 72.00),
    (6015, 'Antiseptic Solution', 40, 95.00),
    (6016, 'Ibuprofen 200 mg', 70, 32.00),
    (6017, 'Electrolyte Powder', 100, 25.00),
    (6018, 'Artificial Tears Eye Drops', 45, 115.00),
    (6019, 'Calcium Supplement Tablets', 65, 82.00),
    (6020, 'Saline Nebulization Solution', 50, 55.00);


-- =====================================================

-- 8. PRESCRIPTIONS: 20 RECORDS

-- Primary keys: 7001-7020

-- =====================================================

INSERT INTO prescriptions (id, appointment_id, medicine_details, dosage) VALUES
    (7001, 3001, 'Cetirizine 10 mg', 'As prescribed by the treating clinician'),
    (7002, 3002, 'Amlodipine 5 mg', 'As prescribed by the treating clinician'),
    (7003, 3003, 'Calcium + Vitamin D Tablets', 'As prescribed by the treating clinician'),
    (7004, 3004, 'ORS Sachets', 'As prescribed by the treating clinician'),
    (7005, 3005, 'Omeprazole 20 mg', 'As prescribed by the treating clinician'),
    (7006, 3006, 'Metformin 500 mg', 'As prescribed by the treating clinician'),
    (7007, 3007, 'Paracetamol 500 mg', 'As prescribed by the treating clinician'),
    (7008, 3008, 'Calcium Supplement Tablets', 'As prescribed by the treating clinician'),
    (7009, 3009, 'Cough Relief Syrup', 'As prescribed by the treating clinician'),
    (7010, 3010, 'Lubricating Eye Drops', 'As prescribed by the treating clinician'),
    (7011, 3011, 'Amlodipine 5 mg', 'As prescribed by the treating clinician'),
    (7012, 3012, 'Paracetamol 500 mg', 'As prescribed by the treating clinician'),
    (7013, 3013, 'Pantoprazole 40 mg', 'As prescribed by the treating clinician'),
    (7014, 3014, 'Cetirizine 10 mg', 'As prescribed by the treating clinician'),
    (7015, 3015, 'Calcium + Vitamin D Tablets', 'As prescribed by the treating clinician'),
    (7016, 3016, 'Metformin 500 mg', 'As prescribed by the treating clinician'),
    (7017, 3017, 'Paracetamol 500 mg', 'As prescribed by the treating clinician'),
    (7018, 3018, 'Multivitamin Tablets', 'As prescribed by the treating clinician'),
    (7019, 3019, 'Saline Nebulization Solution', 'As prescribed by the treating clinician'),
    (7020, 3020, 'Artificial Tears Eye Drops', 'As prescribed by the treating clinician');


-- =====================================================

-- 9. USERS: EXACTLY 2 ACCOUNTS

-- =====================================================

INSERT INTO users (id, username, password, role) VALUES
    (1, 'Piku', 'Piku@Apollo2026!', 'ADMIN'),
    (2, 'KaaL-EL', 'KaaLEL@Apollo2026!', 'STAFF');

