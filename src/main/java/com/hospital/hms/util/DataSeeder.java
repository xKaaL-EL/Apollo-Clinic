package com.hospital.hms.util;

import com.hospital.hms.model.entity.*;
import com.hospital.hms.model.enums.*;
import com.hospital.hms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final FollowUpRepository followUpRepository;
    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public DataSeeder(UserRepository userRepository,
                      DoctorRepository doctorRepository,
                      PatientRepository patientRepository,
                      AppointmentRepository appointmentRepository,
                      BillRepository billRepository,
                      FollowUpRepository followUpRepository,
                      MedicineRepository medicineRepository,
                      PrescriptionRepository prescriptionRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
        this.followUpRepository = followUpRepository;
        this.medicineRepository = medicineRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed admin user
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("1234");
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Default administrator account seeded: admin/1234");
        }

        // 2. Load and parse legacy data if tables are currently empty
        String basePath = "src/Files/";
        
        // Let's check alternative path if workspace is active
        File testFile = new File(basePath + "doctor.txt");
        if (!testFile.exists()) {
            basePath = "Files/";
        }

        // schema.sql is the startup seed source when legacy flat files are not
        // present. Do not reintroduce a second, stale hard-coded dataset here.
        if (!new File(basePath + "doctor.txt").exists()) {
            System.out.println("Legacy file seed skipped: schema.sql handles startup seed data.");
            return;
        }

        if (doctorRepository.count() == 0) {
            seedDoctors(basePath + "doctor.txt");
        }
        if (patientRepository.count() == 0) {
            seedPatients(basePath + "patient.txt");
        }
        if (appointmentRepository.count() == 0) {
            seedAppointments(basePath + "appointment.txt");
        }
        if (billRepository.count() == 0) {
            seedBills(basePath + "bill.txt");
        }
        if (followUpRepository.count() == 0) {
            seedFollowUps(basePath + "followup.txt");
        }
        if (medicineRepository.count() == 0) {
            seedMedicines(basePath + "medicine.txt");
        }
        if (prescriptionRepository.count() == 0) {
            seedPrescriptions(basePath + "prescription.txt");
        }
    }

    private void seedDoctors(String path) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] fields = line.split(";");
                if (fields.length >= 6) {
                    try {
                        Long id = Long.parseLong(fields[0].trim());
                        String name = fields[1].trim();
                        Gender gender = Gender.valueOf(fields[2].trim().toUpperCase());
                        Double age = Double.parseDouble(fields[3].trim());
                        String spec = fields[4].trim();
                        String contact = fields[5].trim();

                        if (!doctorRepository.existsById(id)) {
                            Doctor doc = new Doctor(id, name, gender, age, spec, contact);
                            doctorRepository.save(doc);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to seed doctor line: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
            System.out.println("Legacy doctor database records imported.");
        } catch (IOException e) {
            System.err.println("Could not read doctor.txt legacy database file.");
        }
    }

    private void seedPatients(String path) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] fields = line.split(";");
                if (fields.length >= 5) {
                    try {
                        Long id = Long.parseLong(fields[0].trim());
                        String name = fields[1].trim();
                        Gender gender = Gender.valueOf(fields[2].trim().toUpperCase());
                        Double age = Double.parseDouble(fields[3].trim());
                        String ailment = fields[4].trim();

                        if (!patientRepository.existsById(id)) {
                            Patient p = new Patient(id, name, gender, age, ailment);
                            patientRepository.save(p);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to seed patient line: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
            System.out.println("Legacy patient database records imported.");
        } catch (IOException e) {
            System.err.println("Could not read patient.txt legacy database file.");
        }
    }

    private void seedAppointments(String path) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] fields = line.split(";");
                if (fields.length >= 5) {
                    try {
                        Long id = Long.parseLong(fields[0].trim());
                        Long patientId = Long.parseLong(fields[1].trim());
                        Long doctorId = Long.parseLong(fields[2].trim());
                        String date = fields[3].trim();
                        String time = fields[4].trim();

                        Patient patient = patientRepository.findById(patientId).orElse(null);
                        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

                        if (patient != null && doctor != null) {
                            if (!appointmentRepository.existsById(id)) {
                                Appointment appt = new Appointment(id, patient, doctor, date, time);
                                appointmentRepository.save(appt);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to seed appointment line: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
            System.out.println("Legacy appointment database records imported.");
        } catch (IOException e) {
            System.err.println("Could not read appointment.txt legacy database file.");
        }
    }

    private void seedBills(String path) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] fields = line.split(";");
                if (fields.length >= 4) {
                    try {
                        Long id = Long.parseLong(fields[0].trim());
                        Long patientId = Long.parseLong(fields[1].trim());
                        Double amount = Double.parseDouble(fields[2].trim());
                        BillStatus status = BillStatus.valueOf(fields[3].trim().toUpperCase());

                        Patient patient = patientRepository.findById(patientId).orElse(null);
                        if (patient != null) {
                            if (!billRepository.existsById(id)) {
                                Bill bill = new Bill(id, patient, amount, status);
                                billRepository.save(bill);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to seed bill line: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read bill.txt legacy database file.");
        }
    }

    private void seedFollowUps(String path) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] fields = line.split(";");
                if (fields.length >= 4) {
                    try {
                        Long id = Long.parseLong(fields[0].trim());
                        Long patientId = Long.parseLong(fields[1].trim());
                        String date = fields[2].trim();
                        String notes = fields[3].trim();

                        Patient patient = patientRepository.findById(patientId).orElse(null);
                        if (patient != null) {
                            if (!followUpRepository.existsById(id)) {
                                FollowUp f = new FollowUp(id, patient, date, notes);
                                followUpRepository.save(f);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to seed follow-up line: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read followup.txt legacy database file.");
        }
    }

    private void seedMedicines(String path) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] fields = line.split(";");
                if (fields.length >= 4) {
                    try {
                        Long id = Long.parseLong(fields[0].trim());
                        String name = fields[1].trim();
                        Integer stock = Integer.parseInt(fields[2].trim());
                        Double price = Double.parseDouble(fields[3].trim());

                        if (!medicineRepository.existsById(id)) {
                            Medicine m = new Medicine(id, name, stock, price);
                            medicineRepository.save(m);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to seed medicine line: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read medicine.txt legacy database file.");
        }
    }

    private void seedPrescriptions(String path) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] fields = line.split(";");
                if (fields.length >= 4) {
                    try {
                        Long id = Long.parseLong(fields[0].trim());
                        Long apptId = Long.parseLong(fields[1].trim());
                        String medDetails = fields[2].trim();
                        String dosage = fields[3].trim();

                        Appointment appt = appointmentRepository.findById(apptId).orElse(null);
                        if (appt != null) {
                            if (!prescriptionRepository.existsById(id)) {
                                Prescription p = new Prescription(id, appt, medDetails, dosage);
                                prescriptionRepository.save(p);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to seed prescription line: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read prescription.txt legacy database file.");
        }
    }
}
