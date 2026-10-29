package com.hospital.hms.controller.web;

import com.hospital.hms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebViewController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final BillService billService;
    private final FollowUpService followUpService;
    private final MedicineService medicineService;
    private final PrescriptionService prescriptionService;

    @Autowired
    public WebViewController(DoctorService doctorService,
                             PatientService patientService,
                             AppointmentService appointmentService,
                             BillService billService,
                             FollowUpService followUpService,
                             MedicineService medicineService,
                             PrescriptionService prescriptionService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.billService = billService;
        this.followUpService = followUpService;
        this.medicineService = medicineService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        // Load dropdown options for relationships to make forms super easy to fill
        model.addAllAttributes(java.util.Map.of(
            "doctorsList", doctorService.getAllDoctors("NAME"),
            "patientsList", patientService.getAllPatients("NAME"),
            "appointmentsList", appointmentService.getAllAppointments("ID"),
            "medicinesList", medicineService.getAllMedicines("NAME")
        ));
        return "dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        return indexPage(model);
    }
}
