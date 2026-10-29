# 🏥 Hospital Management System (HMS) - Spring Boot Edition

A modern, enterprise-ready **Spring Boot (Java 21)** clinical administration web application backed by **MySQL / MariaDB**, featuring a responsive **Thymeleaf & Bootstrap 5** single-page web UI and full REST APIs.

---

## 🏗️ Architecture & Technology Stack

* **Backend Framework:** Spring Boot 3.3.4 (Java 21)
* **Persistence Layer:** Spring Data JPA / Hibernate
* **Database:** MySQL / MariaDB (with automatic H2 profile support for unit testing)
* **View Layer:** Thymeleaf, HTML5, Bootstrap 5, Bootstrap Icons, and Vanilla JavaScript (AJAX SPA)
* **Build Tool:** Gradle / Maven

---

## 📂 Project Structure

```text
Hospital-Management-System/
├── src/main/java/com/hospital/hms/
│   ├── HospitalManagementSystemApplication.java   # Spring Boot Entry Point
│   ├── controller/
│   │   ├── api/                                 # REST Controllers (JSON Endpoints)
│   │   └── web/                                 # Web MVC Controllers (Thymeleaf UI)
│   ├── dto/                                     # Data Transfer Objects
│   ├── exception/                               # Custom Exception Handlers
│   ├── model/
│   │   ├── entity/                              # JPA Entities (Doctor, Patient, etc.)
│   │   └── enums/                               # Gender, BillStatus Enums
│   ├── repository/                              # Spring Data JPA Interfaces
│   ├── service/                                 # Business Logic Interfaces & Impls
│   └── util/                                    # Legacy Data Seeder & Migrator
├── src/main/resources/
│   ├── application.properties                   # MySQL Connection Configuration
│   └── templates/                               # Thymeleaf UI Templates (login.html, dashboard.html)
├── schema.sql                                   # MySQL Database Setup Script
└── build.gradle / pom.xml                       # Gradle & Maven Build Files
```

---

## 🛢️ Database Setup & Connection Process

Follow these steps to configure and connect the application with **MySQL / MariaDB**:

### Step 1: Create the Database & User
Log into your MySQL / MariaDB console:
```sql
CREATE DATABASE IF NOT EXISTS hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Alternatively, you can execute the provided `schema.sql` script directly in your MySQL client:
```bash
mysql -u root -p < schema.sql
```

### Step 2: Configure Application Properties
Open `src/main/resources/application.properties` and verify your database credentials match your local MySQL configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

> **Note:** If your MySQL password is different, update `spring.datasource.password=YOUR_PASSWORD` accordingly.

---

## 🚀 Getting Started & Running the Application

### Prerequisites
* **JDK 21+** installed on your system.
* **MySQL / MariaDB** server running.

### 1. Build the Project
Using Gradle Wrapper:
```bash
./gradlew build
```
*(Or `./gradlew bootRun` directly)*

### 2. Run the Application
Execute the Spring Boot bootRun task:
```bash
./gradlew bootRun
```
* **Auto-Seeding:** On startup, the application automatically seeds default credentials and imports all historical records from legacy flat files (`doctor.txt`, `patient.txt`, `appointment.txt`, etc.).

### 3. Access the Web Portal
Open your web browser and go to:
👉 **`http://localhost:8080`**

* **Default Administrator Login:**
  * **Username:** `admin`
  * **Password:** `1234`

---

## 🛠️ Key Technical Capabilities

* **Dual Interface Support:** Full REST API endpoints (`/api/...`) paired with a responsive Bootstrap 5 web dashboard.
* **Smart Relational Resolution:** Automated mapping of foreign key IDs to real human names (e.g. displaying patient and doctor names instead of raw numeric IDs).
* **Dynamic Sorting & Real-Time Search:** Instant client-side and server-side filtering across all modules (Doctors, Patients, Appointments, Bills, Follow-Ups, Medicine, Prescriptions).
* **Zero-Downtime Migration:** Seamlessly migrates legacy flat-file datasets into relational MySQL tables on boot.
