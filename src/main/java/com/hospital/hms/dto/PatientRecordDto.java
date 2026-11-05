package com.hospital.hms.dto;

import java.util.List;
import java.time.LocalDate;

public class PatientRecordDto {
    private PatientDto patient;
    private List<AppointmentHistory> appointments;
    private List<FollowUpDto> followUps;
    private List<PrescriptionHistory> otherPrescriptions;
    private List<PrescriptionHistory> myPrescriptions;

    public PatientRecordDto(PatientDto patient, List<AppointmentHistory> appointments,
                            List<FollowUpDto> followUps, List<PrescriptionHistory> otherPrescriptions,
                            List<PrescriptionHistory> myPrescriptions) {
        this.patient = patient;
        this.appointments = appointments;
        this.followUps = followUps;
        this.otherPrescriptions = otherPrescriptions;
        this.myPrescriptions = myPrescriptions;
    }

    public PatientDto getPatient() { return patient; }
    public List<AppointmentHistory> getAppointments() { return appointments; }
    public List<FollowUpDto> getFollowUps() { return followUps; }
    public List<PrescriptionHistory> getOtherPrescriptions() { return otherPrescriptions; }
    public List<PrescriptionHistory> getMyPrescriptions() { return myPrescriptions; }

    public static class PrescriptionHistory {
        private Long id;
        private Long appointmentId;
        private String appointmentDate;
        private String doctorName;
        private String medicineDetails;
        private String dosage;
        private LocalDate prescriptionDate;
        private boolean editable;

        public PrescriptionHistory(Long id, Long appointmentId, String appointmentDate, String doctorName,
                                   String medicineDetails, String dosage, LocalDate prescriptionDate, boolean editable) {
            this.id = id;
            this.appointmentId = appointmentId;
            this.appointmentDate = appointmentDate;
            this.doctorName = doctorName;
            this.medicineDetails = medicineDetails;
            this.dosage = dosage;
            this.prescriptionDate = prescriptionDate;
            this.editable = editable;
        }

        public Long getId() { return id; }
        public Long getAppointmentId() { return appointmentId; }
        public String getAppointmentDate() { return appointmentDate; }
        public String getDoctorName() { return doctorName; }
        public String getMedicineDetails() { return medicineDetails; }
        public String getDosage() { return dosage; }
        public LocalDate getPrescriptionDate() { return prescriptionDate; }
        public boolean isEditable() { return editable; }
    }

    public static class AppointmentHistory {
        private Long id;
        private String date;
        private String time;
        private Long doctorId;
        private String doctorName;
        private String doctorSpecialization;
        private String ailment;
        private boolean clinicalDetailsVisible;

        public AppointmentHistory(Long id, String date, String time, Long doctorId, String doctorName,
                                   String doctorSpecialization, String ailment, boolean clinicalDetailsVisible) {
            this.id = id; this.date = date; this.time = time; this.doctorId = doctorId;
            this.doctorName = doctorName; this.doctorSpecialization = doctorSpecialization;
            this.ailment = ailment; this.clinicalDetailsVisible = clinicalDetailsVisible;
        }

        public Long getId() { return id; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public Long getDoctorId() { return doctorId; }
        public String getDoctorName() { return doctorName; }
        public String getDoctorSpecialization() { return doctorSpecialization; }
        public String getAilment() { return ailment; }
        public boolean isClinicalDetailsVisible() { return clinicalDetailsVisible; }
    }
}
