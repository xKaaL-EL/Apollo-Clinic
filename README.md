<div align="center">

# 🏥 Apollo Clinic Portal

**A comprehensive, role-based clinic management and electronic healthcare system built with Java 21, Spring Boot, MySQL, and OpenPDF.**

Manage clinical operations, doctor workflows, patient electronic health records (EHR), appointments, prescriptions, follow-ups, inventory, and billing invoices from one secure, cohesive platform.

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?style=flat&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?style=flat&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3.2-7952B3?style=flat&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>

---

## 📖 Table of Contents

- [About the Project](#-about-the-project)
- [System Architecture](#-system-architecture)
- [What It Can Do](#-what-it-can-do)
- [User Roles & Access Control (RBAC)](#-user-roles--access-control-rbac)
- [Clinical Workspaces & Key Modules](#-clinical-workspaces--key-modules)
  - [1. Doctor Workspace & Dashboard](#1-doctor-workspace--dashboard)
  - [2. Patient Medical Record (EHR)](#2-patient-medical-record-ehr)
  - [3. Doctor Account & Profile Management](#3-doctor-account--profile-management)
  - [4. Appointment & Follow-Up Management](#4-appointment--follow-up-management)
  - [5. Prescription Management & PDF Generation](#5-prescription-management--pdf-generation)
  - [6. Billing & Invoice PDF Generation](#6-billing--invoice-pdf-generation)
  - [7. Medicine Inventory](#7-medicine-inventory)
- [Technology Stack](#-technology-stack)
- [Database Schema & Data Model](#-database-schema--data-model)
- [Demo Credentials](#-demo-credentials)
- [Prerequisites](#-prerequisites)
- [Local Setup & Installation](#-local-setup--installation)
- [REST API Reference](#-rest-api-reference)
- [Testing & Building](#-testing--building)
- [Project Directory Layout](#-project-directory-layout)
- [Screenshots](#-screenshots)

---

## 📌 About the Project

**Apollo Clinic Portal** is a production-grade healthcare administration and clinical coordination web application. Designed for modern multi-specialty outpatient clinics, it streamlines day-to-day administrative operations while providing physicians with dedicated, context-aware clinical workflows.

The system features robust session-based authentication, granular **Role-Based Access Control (RBAC)** across three distinct personas (`ADMIN`, `RECEPTIONIST`, and `DOCTOR`), live searchable and sortable data tables, comprehensive patient medical histories, and vector-rendered PDF generation for official prescriptions and billing invoices.

---

## 🏛️ System Architecture

The application adheres to clean layered architectural principles with clear separation of concerns:

```
[ Web Browser / Client ]
       │
       ▼ HTTP / REST
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  - Thymeleaf SSR (Login, Dashboards, EHR Views)             │
│  - Bootstrap 5 Responsive UI & Custom Portal Theme          │
│  - Asynchronous SPA Interactions (Vanilla ES6+ JavaScript)  │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                   Web & Security Layer                      │
│  - Spring MVC Controllers (WebViewController)               │
│  - REST API Controllers (/api/auth, /api/doctors, etc.)     │
│  - RoleAuthorizationInterceptor (Route & Method Guard)      │
│  - HTTP Session Lifecycle Management                        │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                     Service Layer                           │
│  - Business Logic Services (DoctorService, PatientService)  │
│  - DoctorAccessService (Data Ownership & Access Security)   │
│  - PatientRecordService (EHR Aggregation & Timeline)        │
│  - PdfDocumentService (OpenPDF Vector Document Engine)      │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                  Data Persistence Layer                     │
│  - Spring Data JPA Repositories                             │
│  - Hibernate ORM / Entity Mapping                           │
│  - Relational Database (MySQL 8+ / MariaDB / H2 Testing)    │
│  - Idempotent SQL Migrations & Data Seeding (schema.sql)    │
└─────────────────────────────────────────────────────────────┘
```

---

## ✨ What It Can Do

- 🔐 **Multi-Role Authentication:** Stateful, session-backed authentication supporting `ADMIN`, `RECEPTIONIST`, and `DOCTOR` users with intelligent role-based dashboard redirection.
- 🛡️ **Role-Based Access Control (RBAC):** Server-side enforcement with `RoleAuthorizationInterceptor` combined with dynamic UI element hiding and data isolation.
- 🧑‍⚕️ **Doctor Account Provisioning:** Administrators can create and link user login accounts for doctors, set initial credentials, and update doctor profiles.
- 🩺 **Dedicated Doctor Dashboard:** A streamlined workspace displaying personal appointments, today's schedule with chronological time sorting, real-time date/time filtering, and one-click access to patient health records.
- 🗂️ **Patient Electronic Health Records (EHR):** Comprehensive medical record interface showing patient demographics, full appointment history, follow-up schedules, and separated prescription histories.
- 💊 **Prescription History Segregation:** Doctors view patient prescriptions organized into **"My Prescriptions"** (full editing and deletion authority) and **"Other Prescriptions"** (read-only historical visibility across other clinic physicians).
- 🔁 **Appointment-Linked Follow-Ups:** Schedule next consultation dates and clinical progress notes directly tied to specific patient visits.
- 📄 **Branded PDF Generation:** On-the-fly vector PDF rendering for clinical prescriptions and official billing invoices with inline browser preview and direct download capabilities.
- 🧾 **Billing & Accounts:** Manage patient charges and monitor settlement statuses (`UNPAID`, `PAID`, `OVERDUE`).
- 📦 **Medicine Inventory:** Track pharmaceutical stock levels, dosages, and unit pricing.
- 🔍 **Real-Time Search & Sorting:** Instant search filtering and multi-attribute sorting across all clinical entities without full-page reloads.
- 🗄️ **Resilient Database Layer:** Non-destructive database migrations with schema idempotency and automated seed data fallback.

---

## 👥 User Roles & Access Control (RBAC)

The portal enforces strict separation of responsibilities among three system roles:

| Module / Resource | ADMIN | RECEPTIONIST | DOCTOR |
| :--- | :---: | :---: | :---: |
| **Doctor Records** | Full CRUD + Account Provisioning | View Only | View Only (Public Directory) |
| **Doctor Self-Profile** | Edit via Doctor Directory | ❌ Denied | View & Edit Own Profile (`/api/doctors/me`) |
| **Doctor Workspace** | ❌ (Redirected to Admin Dashboard) | ❌ (Redirected to Admin Dashboard) | Full Access (`/doctor/dashboard`) |
| **Patient Records** | Full CRUD | Full CRUD | View Assigned Patients' EHR (`/patient-record/{id}`) |
| **Appointments** | Full CRUD | Full CRUD | View Assigned Appointments (`/api/appointments/doctor/mine`) |
| **Prescriptions** | Full CRUD + PDF | View Only + PDF View/Download | Create/Edit/Delete Own Prescriptions; View Others + PDF |
| **Follow-Ups** | Full CRUD | View Only | Create/Edit/Delete Own Linked Follow-ups |
| **Billing & Invoices** | Full CRUD + PDF | View Only + PDF View/Download | ❌ Denied (403 Forbidden) |
| **Medicine Inventory** | Full CRUD | View Only | ❌ Denied (403 Forbidden) |

### Authorization Enforcement Details

1. **Server-Side Interception:** Every incoming request is validated by `RoleAuthorizationInterceptor`. Unauthenticated sessions are redirected to `/login` (or given `401 Unauthorized` for REST calls). Forbidden operations immediately return `403 Forbidden`.
2. **Physician Data Isolation (`DoctorAccessService`):**
   - Doctors can only view appointments assigned to their specific doctor account.
   - Doctors can only access EHR records (`/patient-record/{patientId}`) for patients who have an appointment scheduled with them.
   - Prescriptions and follow-up entries can only be created, modified, or deleted by the doctor who owns the corresponding appointment.

---

## 🩺 Clinical Workspaces & Key Modules

### 1. Doctor Workspace & Dashboard

Navigating to `/doctor/dashboard` presents physicians with a clean clinical workspace tailored to outpatient consultation:

- **Today's Appointments:** Automatically filters and displays patients booked for the current date. Includes an interactive table header sort button (`↑` / `↓`) for instant chronological time ordering.
- **My Appointments:** Lists all historical and future appointments assigned to the logged-in doctor. Features dynamic filter inputs for specific **Date** and **Time** along with a quick-reset action.
- **Direct Patient Record Navigation:** Every appointment row features a **"View"** button that opens the patient's comprehensive medical record in a dedicated tab with full appointment context.

### 2. Patient Medical Record (EHR)

Accessible at `/patient-record/{patientId}?appointmentId={appointmentId}`, this view delivers a 360-degree clinical summary:

- **Patient Demographics:** Patient ID, full name, age, gender, and primary ailment.
- **Current Appointment Action Banner:** When opened from an active consultation, displays appointment details and provides rapid-action buttons: **"New Prescription"** and **"Create Follow-up"**.
- **Appointment History:** Timeline of all past and upcoming visits, doctor names, medical specializations, and clinical notes.
- **Separated Prescription Sections:**
  - **"My Prescriptions":** Prescriptions issued by the logged-in doctor. Doctors can update medication details, adjust dosage, delete entries, or generate PDFs.
  - **"Other Prescriptions":** Prescriptions issued to the same patient by other clinic specialists, providing full cross-specialty clinical history in read-only mode with PDF view access.
- **Follow-Up History:** Scheduled next visit dates and clinical progress notes with role-sensitive edit/delete actions.

### 3. Doctor Account & Profile Management

- **Admin Provisioning:** When administrators add or edit a doctor in the main portal, they configure the doctor's login credentials (`username`, `password`, and `confirmPassword`). The system links the `doctors` table row to a new `users` record via `account_id`.
- **Doctor Self-Service Profile:** Doctors can click the **"My Profile"** button in their dashboard header to inspect their account details. They can independently update their **username**, change their **password** (with confirmation), and revise their **specialization** and **contact number** via `PUT /api/doctors/me`. Non-editable fields (Name, Age, Gender) remain protected.

### 4. Appointment & Follow-Up Management

- **Appointments:** Schedule and coordinate consultations between patients and doctors with precise date and time tracking.
- **Follow-Ups:** Schedule future review visits and log recovery progress notes. Follow-ups can be optionally associated with an `appointment_id` to establish clinical continuity.

### 5. Prescription Management & PDF Generation

- Prescriptions store medication details, specific dosage instructions (e.g., `1 tab x 3 times daily`), prescription date, and the parent appointment reference.
- **High-Quality PDF Generation:** Powered by OpenPDF, documents feature official clinic letterheads, patient demographics, attending physician details, structured medication instructions, and follow-up schedules. Available for viewing inline or direct file download.

### 6. Billing & Invoice PDF Generation

- Manages patient bills with precise amount tracking and lifecycle payment states (`UNPAID`, `PAID`, `OVERDUE`).
- Generates branded invoice PDFs containing invoice numbers, itemized charge breakdowns, and color-coded status badges (green for Paid, red for Unpaid, deep red for Overdue).

### 7. Medicine Inventory

- Tracks clinic pharmacy supplies, unit stock quantities, and retail pricing.
- Provides search and sorting capabilities by stock quantity, price, and medicine name.

---

## 🧰 Technology Stack

| Layer | Technology | Purpose |
| :--- | :--- | :--- |
| **Language** | Java 21 | Modern LTS core application programming language |
| **Framework** | Spring Boot 3.3.4 | Application container, dependency injection, and configuration |
| **Web & MVC** | Spring MVC | REST API endpoints, session handling, and view controllers |
| **Persistence** | Spring Data JPA / Hibernate | Object-Relational Mapping (ORM) and repository abstractions |
| **Primary Database** | MySQL 8+ / MariaDB | Relational persistence for production and local development |
| **Test Database** | H2 Database | In-memory relational database for isolated test execution |
| **Templating** | Thymeleaf 3 | Server-side HTML view rendering |
| **Styling & UI** | Bootstrap 5.3.2 & Icons 1.11.1 | Responsive layout, modal dialogs, and UI design |
| **Document Engine**| OpenPDF 2.0.3 | High-fidelity vector PDF rendering for prescriptions and invoices |
| **Build Tool** | Gradle Wrapper (Gradle 8+) | Build automation, dependency management, and test harness |

---

## 🗄️ Database Schema & Data Model

The schema is defined in `schema.sql` with idempotent table definitions and migration-safe constraints:

```mermaid
erDiagram
    users ||--o| doctors : "account_id (1:1)"
    patients ||--o{ appointments : "patient_id"
    doctors ||--o{ appointments : "doctor_id"
    patients ||--o{ bills : "patient_id"
    patients ||--o{ followups : "patient_id"
    appointments ||--o{ followups : "appointment_id"
    appointments ||--o{ prescriptions : "appointment_id"

    users {
        bigint id PK
        varchar username UK
        varchar password
        varchar role
    }
    doctors {
        bigint id PK
        varchar name
        varchar gender
        double age
        varchar specialization
        varchar contact
        bigint account_id FK,UK
    }
    patients {
        bigint id PK
        varchar name
        varchar gender
        double age
        varchar ailment
    }
    appointments {
        bigint id PK
        bigint patient_id FK
        bigint doctor_id FK
        varchar date
        varchar time
    }
    bills {
        bigint id PK
        bigint patient_id FK
        double amount
        varchar status
    }
    followups {
        bigint id PK
        bigint patient_id FK
        bigint appointment_id FK
        varchar next_date
        text notes
    }
    medicines {
        bigint id PK
        varchar name
        int stock
        double price
    }
    prescriptions {
        bigint id PK
        bigint appointment_id FK
        varchar medicine_details
        varchar dosage
        date prescription_date
    }
```

---

## 🔐 Demo Credentials

The application seeds initial data upon first startup via `schema.sql` (and `DataSeeder.java`). The following accounts are available for testing:

| Role | Username | Password | Notes / Specialty |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `Piku` | `Piku@Apollo2026!` | Primary clinic administrator (Full system access) |
| **ADMIN (Fallback)**| `admin` | `1234` | Default fallback seeded by `DataSeeder` |
| **RECEPTIONIST** | `KaaL-EL` | `KaaLEL@Apollo2026!` | Front-desk staff account (Patient/Appt management) |
| **DOCTOR** | `aarav.sharma` | `Aarav@Apollo2026!` | Dr. Aarav Sharma — Cardiology (ID: 1001) |
| **DOCTOR** | `ananya.verma` | `Ananya@Apollo2026!` | Dr. Ananya Verma — Dermatology (ID: 1002) |
| **DOCTOR** | `rohan.mehta` | `Rohan@Apollo2026!` | Dr. Rohan Mehta — Orthopedics (ID: 1003) |
| **DOCTOR** | `priya.nair` | `Priya@Apollo2026!` | Dr. Priya Nair — Pediatrics (ID: 1004) |
| **DOCTOR** | `arjun.kulkarni`| `Arjun@Apollo2026!` | Dr. Arjun Kulkarni — General Medicine (ID: 1005) |

> [!NOTE]
> Additional doctor accounts follow the convention `firstname.lastname` with password `Firstname@Apollo2026!` as defined in `schema.sql`.

---

## 💻 Prerequisites

Ensure you have installed:

- **Java Development Kit (JDK) 21** or higher.
- **MySQL 8.0+** or **MariaDB 10.5+**.
- A modern web browser (Google Chrome, Mozilla Firefox, Microsoft Edge, Safari).

*You do not need to install Gradle manually; the project includes the Gradle Wrapper (`gradlew` / `gradlew.bat`).*

---

## 🚀 Local Setup & Installation

### 1. Clone the Repository

```bash
git clone https://github.com/xKaaL-EL/Apollo-Clinic.git
cd Apollo-Clinic
```

### 2. Create the MySQL Database

Start your local MySQL / MariaDB service and execute:

```sql
CREATE DATABASE IF NOT EXISTS hospital_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Database Credentials

Configure your database credentials using environment variables or customize `src/main/resources/application.properties`.

**Recommended Environment Variables:**

```bash
# On Linux / macOS:
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export SPRING_DATASOURCE_USERNAME="your_mysql_username"
export SPRING_DATASOURCE_PASSWORD="your_mysql_password"

# On Windows (PowerShell):
$env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:SPRING_DATASOURCE_USERNAME="your_mysql_username"
$env:SPRING_DATASOURCE_PASSWORD="your_mysql_password"
```

The application's default `application.properties` reads these environment variables:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:root123}
spring.sql.init.mode=always
spring.sql.init.schema-locations=file:./schema.sql
spring.jpa.hibernate.ddl-auto=update
```

### 4. Run the Application

Execute the Gradle Wrapper boot task:

**Windows (PowerShell / Command Prompt):**

```powershell
.\gradlew.bat bootRun
```

**Linux / macOS:**

```bash
./gradlew bootRun
```

Once started, navigate to:

```text
http://localhost:8080/login
```

Log in using one of the [Demo Credentials](#-demo-credentials). The portal will automatically route you to the appropriate dashboard:
- **Admin / Receptionist:** `http://localhost:8080/dashboard`
- **Doctor:** `http://localhost:8080/doctor/dashboard`

---

## 🔌 REST API Reference

### 1. Authentication Endpoints

| Method | Endpoint | Description | Permitted Roles |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Authenticate user credentials and create session | Public |
| `GET` | `/api/auth/me` | Fetch currently logged-in user profile & role | Authenticated |
| `POST` | `/api/auth/logout` | Invalidate session and log out | Authenticated |

### 2. Clinical & Doctor Endpoints

| Method | Endpoint | Description | Permitted Roles |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/doctors/me` | Retrieve the logged-in doctor's profile | `DOCTOR` |
| `PUT` | `/api/doctors/me` | Update doctor's own credentials and contact info | `DOCTOR` |
| `GET` | `/api/appointments/doctor/mine` | Retrieve all appointments assigned to logged-in doctor | `DOCTOR` |
| `GET` | `/api/patient-records/{id}` | Retrieve patient EHR (demographics, history, prescriptions) | `DOCTOR` |

### 3. Core Resource Endpoints

| Resource | Base Endpoint | Supported Methods | Description |
| :--- | :--- | :--- | :--- |
| **Doctors** | `/api/doctors` | `GET`, `POST`, `PUT`, `DELETE` | Directory and doctor account management |
| **Patients** | `/api/patients` | `GET`, `POST`, `PUT`, `DELETE` | Patient admission and records |
| **Appointments** | `/api/appointments` | `GET`, `POST`, `PUT`, `DELETE` | Clinic appointment scheduling |
| **Prescriptions** | `/api/prescriptions` | `GET`, `POST`, `PUT`, `DELETE` | Clinical medication issuance |
| **Follow-Ups** | `/api/followups` | `GET`, `POST`, `PUT`, `DELETE` | Review visits and progress tracking |
| **Bills** | `/api/bills` | `GET`, `POST`, `PUT`, `DELETE` | Patient billing and payment statuses |
| **Medicines** | `/api/medicines` | `GET`, `POST`, `PUT`, `DELETE` | Pharmacy inventory control |

*Search query support is available for all resources at `/api/{resource}/search?query={keyword}`.*

### 4. PDF Generation Endpoints

| Document | Inline Preview URL | File Download URL | Access |
| :--- | :--- | :--- | :--- |
| **Prescription PDF** | `GET /api/prescriptions/{id}/pdf` | `GET /api/prescriptions/{id}/pdf/download` | Admin, Receptionist, Assigned Doctor |
| **Billing Invoice PDF** | `GET /api/bills/{id}/pdf` | `GET /api/bills/{id}/pdf/download` | Admin, Receptionist |

---

## 🧪 Testing & Building

### Run the Test Suite

The test suite runs against an isolated, in-memory H2 database requiring no running MySQL instance:

**Windows:**

```powershell
.\gradlew.bat test
```

**Linux / macOS:**

```bash
./gradlew test
```

### Build Executable JAR

Generate the standalone Spring Boot executable JAR:

**Windows:**

```powershell
.\gradlew.bat build
```

**Linux / macOS:**

```bash
./gradlew build
```

The runnable artifact is output to:

```text
build/libs/hospital-management-system-1.0.0.jar
```

Run the built JAR using:

```bash
java -jar build/libs/hospital-management-system-1.0.0.jar
```

---

## 📂 Project Directory Layout

```text
Apollo-Clinic/
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── ScreenShots/
│   ├── login.png
│   ├── Screenshot (19).png
│   ├── Screenshot (25).png
│   ├── Screenshot (44).png
│   ├── Screenshot (45).png
│   ├── Screenshot (46).png
│   └── Screenshot (48).png
├── src/
│   ├── main/
│   │   ├── java/com/hospital/hms/
│   │   │   ├── config/
│   │   │   │   ├── RoleAuthorizationInterceptor.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── api/
│   │   │   │   │   ├── AppointmentApiController.java
│   │   │   │   │   ├── AuthApiController.java
│   │   │   │   │   ├── BillApiController.java
│   │   │   │   │   ├── DoctorApiController.java
│   │   │   │   │   ├── FollowUpApiController.java
│   │   │   │   │   ├── MedicineApiController.java
│   │   │   │   │   ├── PatientApiController.java
│   │   │   │   │   ├── PatientRecordApiController.java
│   │   │   │   │   └── PrescriptionApiController.java
│   │   │   │   └── web/
│   │   │   │       └── WebViewController.java
│   │   │   ├── dto/
│   │   │   │   ├── AppointmentDto.java
│   │   │   │   ├── BillDto.java
│   │   │   │   ├── DoctorDto.java
│   │   │   │   ├── DoctorProfileUpdateDto.java
│   │   │   │   ├── FollowUpDto.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── MedicineDto.java
│   │   │   │   ├── PatientDto.java
│   │   │   │   ├── PatientRecordDto.java
│   │   │   │   └── PrescriptionDto.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── model/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── Appointment.java
│   │   │   │   │   ├── Bill.java
│   │   │   │   │   ├── Doctor.java
│   │   │   │   │   ├── FollowUp.java
│   │   │   │   │   ├── Medicine.java
│   │   │   │   │   ├── Patient.java
│   │   │   │   │   ├── Prescription.java
│   │   │   │   │   └── User.java
│   │   │   │   └── enums/
│   │   │   │       ├── BillStatus.java
│   │   │   │       └── Gender.java
│   │   │   ├── repository/
│   │   │   │   ├── AppointmentRepository.java
│   │   │   │   ├── BillRepository.java
│   │   │   │   ├── DoctorRepository.java
│   │   │   │   ├── FollowUpRepository.java
│   │   │   │   ├── MedicineRepository.java
│   │   │   │   ├── PatientRepository.java
│   │   │   │   ├── PrescriptionRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/
│   │   │   │   ├── impl/
│   │   │   │   │   ├── AppointmentServiceImpl.java
│   │   │   │   │   ├── BillServiceImpl.java
│   │   │   │   │   ├── DoctorServiceImpl.java
│   │   │   │   │   ├── FollowUpServiceImpl.java
│   │   │   │   │   ├── MedicineServiceImpl.java
│   │   │   │   │   ├── PatientRecordServiceImpl.java
│   │   │   │   │   ├── PatientServiceImpl.java
│   │   │   │   │   ├── PrescriptionServiceImpl.java
│   │   │   │   │   └── UserServiceImpl.java
│   │   │   │   ├── AppointmentService.java
│   │   │   │   ├── BillService.java
│   │   │   │   ├── DoctorAccessService.java
│   │   │   │   ├── DoctorService.java
│   │   │   │   ├── FollowUpService.java
│   │   │   │   ├── MedicineService.java
│   │   │   │   ├── PatientRecordService.java
│   │   │   │   ├── PatientService.java
│   │   │   │   ├── PdfBranding.java
│   │   │   │   ├── PdfDocumentService.java
│   │   │   │   ├── PrescriptionService.java
│   │   │   │   └── UserService.java
│   │   │   ├── util/
│   │   │   │   └── DataSeeder.java
│   │   │   └── HospitalManagementSystemApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/portal.css
│   │       │   └── js/
│   │       │       ├── doctor-dashboard-enhancements.js
│   │       │       ├── patient-record-enhancements.js
│   │       │       └── portal.js
│   │       ├── templates/
│   │       │   ├── dashboard.html
│   │       │   ├── doctor-dashboard.html
│   │       │   ├── login.html
│   │       │   └── patient-record.html
│   │       ├── application.properties
│   │       └── schema.sql
│   └── test/
│       ├── java/com/hospital/hms/
│       │   └── HospitalManagementSystemApplicationTests.java
│       └── resources/
│           └── application-test.properties
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── schema.sql
└── README.md
```

---

## 📸 Screenshots

### Login Page
![Apollo Clinic Portal login page](ScreenShots/login.png)

### Doctors Management
![Doctors management screen](ScreenShots/Screenshot%20(19).png)

### Patients Management
![Patients management screen](ScreenShots/Screenshot%20(25).png)

### Billing Management
![Billing management screen](ScreenShots/Screenshot%20(44).png)

### Prescription Management
![Prescription management screen](ScreenShots/Screenshot%20(45).png)

### Medicine Inventory
![Medicine inventory screen](ScreenShots/Screenshot%20(46).png)

### Appointments Management
![Appointments management screen](ScreenShots/Screenshot%20(48).png)

---

<div align="center">

**❤️ Apollo Clinic Portal**

*Built to make everyday clinic administration and patient care seamless, secure, and simple.*

</div>
