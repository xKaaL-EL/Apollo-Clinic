package com.hospital.hms.service;

import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Bill;
import com.hospital.hms.model.entity.FollowUp;
import com.hospital.hms.model.entity.Prescription;
import com.hospital.hms.model.enums.BillStatus;
import com.hospital.hms.repository.BillRepository;
import com.hospital.hms.repository.FollowUpRepository;
import com.hospital.hms.repository.PrescriptionRepository;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PdfDocumentService {

    private static final Color NAVY = new Color(20, 47, 78);
    private static final Color TEAL = new Color(0, 128, 128);
    private static final Color LIGHT_TEAL = new Color(224, 242, 241);
    private static final Color LIGHT_BLUE = new Color(235, 242, 249);
    private static final Color GREEN = new Color(46, 125, 50);
    private static final Color RED = new Color(198, 40, 40);
    private static final Color OVERDUE_RED = new Color(183, 28, 28);

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, NAVY);
    private static final Font BRAND_FONT = new Font(Font.HELVETICA, 15, Font.BOLD, NAVY);
    private static final Font SECTION_FONT = new Font(Font.HELVETICA, 12, Font.BOLD, NAVY);
    private static final Font LABEL_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, NAVY);
    private static final Font BODY_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY);
    private static final Font WHITE_LABEL_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font TOTAL_AMOUNT_FONT = new Font(Font.HELVETICA, 11, Font.BOLD, GREEN);
    private static final Font GENERATED_PRESCRIPTION_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL, TEAL);
    private static final Font GENERATED_INVOICE_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL, GREEN);
    private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("0.00");
    private static final DateTimeFormatter GENERATED_ON_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH);

    private final PrescriptionRepository prescriptionRepository;
    private final BillRepository billRepository;
    private final FollowUpRepository followUpRepository;

    public PdfDocumentService(PrescriptionRepository prescriptionRepository,
                              BillRepository billRepository,
                              FollowUpRepository followUpRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.billRepository = billRepository;
        this.followUpRepository = followUpRepository;
    }

    @Transactional(readOnly = true)
    public byte[] generatePrescriptionPdf(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        String generatedOn = GENERATED_ON_FORMAT.format(ZonedDateTime.now(ZoneId.systemDefault()));

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 42, 42, 42, 42);
            PdfWriter.getInstance(document, output);
            document.open();

            addHeading(document, "Prescription");
            addKeyValueTable(document, new String[][]{
                    {"Prescription ID", value(prescription.getId())},
                    {"Appointment ID", value(prescription.getAppointment().getId())},
                    {"Appointment Date", value(prescription.getAppointment().getDate())},
                    {"Appointment Time", value(prescription.getAppointment().getTime())}
            });

            addSection(document, "Patient");
            addKeyValueTable(document, new String[][]{
                    {"Patient ID", value(prescription.getAppointment().getPatient().getId())},
                    {"Name", value(prescription.getAppointment().getPatient().getName())},
                    {"Gender", value(prescription.getAppointment().getPatient().getGender())},
                    {"Age", value(prescription.getAppointment().getPatient().getAge())},
                    {"Ailment", value(prescription.getAppointment().getPatient().getAilment())}
            });

            addSection(document, "Doctor");
            addKeyValueTable(document, new String[][]{
                    {"Doctor ID", value(prescription.getAppointment().getDoctor().getId())},
                    {"Name", value(prescription.getAppointment().getDoctor().getName())},
                    {"Specialization", value(prescription.getAppointment().getDoctor().getSpecialization())},
                    {"Contact", value(prescription.getAppointment().getDoctor().getContact())}
            });

            addSection(document, "Medication Instructions");
            addKeyValueTable(document, new String[][]{
                    {"Medicine Details", value(prescription.getMedicineDetails())},
                    {"Dosage", value(prescription.getDosage())}
            });

            addSection(document, "Follow-Up Details");
            List<FollowUp> followUps = followUpRepository.findAllByPatientId(
                    prescription.getAppointment().getPatient().getId());
            if (followUps.isEmpty()) {
                addKeyValueTable(document, new String[][]{
                        {"Follow-Up", "No follow-up records available"}
                });
            } else {
                PdfPTable followUpTable = new PdfPTable(new float[]{1.2f, 2.8f});
                followUpTable.setWidthPercentage(100);
                addCell(followUpTable, "Next Date", WHITE_LABEL_FONT, TEAL);
                addCell(followUpTable, "Notes", WHITE_LABEL_FONT, TEAL);
                for (FollowUp followUp : followUps) {
                    addCell(followUpTable, value(followUp.getNextDate()), BODY_FONT, null);
                    addCell(followUpTable, value(followUp.getNotes()), BODY_FONT, null);
                }
                document.add(followUpTable);
            }

            addGeneratedOn(document, generatedOn, GENERATED_PRESCRIPTION_FONT);

            document.close();
            return output.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException("Could not generate prescription PDF", exception);
        }
    }

    @Transactional(readOnly = true)
    public byte[] generateBillPdf(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        String generatedOn = GENERATED_ON_FORMAT.format(ZonedDateTime.now(ZoneId.systemDefault()));

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 42, 42, 42, 42);
            PdfWriter.getInstance(document, output);
            document.open();

            addHeading(document, "Invoice");
            addKeyValueTable(document, new String[][]{
                    {"Invoice ID", value(bill.getId())}
            });
            Font paymentStatusFont = paymentStatusFont(bill.getStatus());
            addKeyValueTable(document, new String[][]{
                    {"Payment Status", value(bill.getStatus())}
            }, paymentStatusFont);

            addSection(document, "Billed Patient");
            addKeyValueTable(document, new String[][]{
                    {"Patient ID", value(bill.getPatient().getId())},
                    {"Name", value(bill.getPatient().getName())},
                    {"Gender", value(bill.getPatient().getGender())},
                    {"Age", value(bill.getPatient().getAge())},
                    {"Ailment", value(bill.getPatient().getAilment())}
            });

            addSection(document, "Charges");
            PdfPTable charges = new PdfPTable(2);
            charges.setWidthPercentage(100);
            addCell(charges, "Description", WHITE_LABEL_FONT, NAVY);
            addCell(charges, "Amount", WHITE_LABEL_FONT, NAVY);
            addCell(charges, "Total bill amount", LABEL_FONT, LIGHT_BLUE);
            addCell(charges, "$" + AMOUNT_FORMAT.format(bill.getAmount()), TOTAL_AMOUNT_FONT, LIGHT_TEAL);
            document.add(charges);

            Paragraph status = new Paragraph("Payment Status: " + value(bill.getStatus()), paymentStatusFont);
            status.setSpacingBefore(12);
            status.setAlignment(Element.ALIGN_RIGHT);
            document.add(status);

            addGeneratedOn(document, generatedOn, GENERATED_INVOICE_FONT);

            document.close();
            return output.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException("Could not generate invoice PDF", exception);
        }
    }

    private void addHeading(Document document, String title) throws DocumentException {
        PdfPTable brand = new PdfPTable(1);
        brand.setWidthPercentage(100);
        PdfPCell brandCell = new PdfPCell();
        brandCell.setBackgroundColor(LIGHT_TEAL);
        brandCell.setBorderColor(TEAL);
        brandCell.setPadding(10);
        Paragraph hospital = new Paragraph(PdfBranding.HOSPITAL_NAME, BRAND_FONT);
        hospital.setAlignment(Element.ALIGN_CENTER);
        brandCell.addElement(hospital);
        Paragraph address = new Paragraph(PdfBranding.ADDRESS + " | " + PdfBranding.CONTACT, BODY_FONT);
        address.setAlignment(Element.ALIGN_CENTER);
        brandCell.addElement(address);
        brand.addCell(brandCell);
        document.add(brand);

        Paragraph heading = new Paragraph(title, TITLE_FONT);
        heading.setAlignment(Element.ALIGN_CENTER);
        heading.setSpacingBefore(14);
        heading.setSpacingAfter(18);
        document.add(heading);
    }

    private void addSection(Document document, String title) throws DocumentException {
        PdfPTable sectionTable = new PdfPTable(1);
        sectionTable.setWidthPercentage(100);
        sectionTable.setSpacingBefore(14);
        PdfPCell sectionCell = new PdfPCell(new Phrase(title, SECTION_FONT));
        sectionCell.setBackgroundColor(LIGHT_TEAL);
        sectionCell.setBorderColor(TEAL);
        sectionCell.setPadding(6);
        sectionTable.addCell(sectionCell);
        document.add(sectionTable);
    }

    private void addKeyValueTable(Document document, String[][] values) throws DocumentException {
        addKeyValueTable(document, values, BODY_FONT);
    }

    private void addKeyValueTable(Document document, String[][] values, Font valueFont) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{1.2f, 2.8f});
        table.setWidthPercentage(100);
        for (String[] row : values) {
            addCell(table, row[0], LABEL_FONT, LIGHT_BLUE);
            addCell(table, row[1], valueFont, null);
        }
        document.add(table);
    }

    private Font paymentStatusFont(BillStatus status) {
        Color color = switch (status) {
            case PAID -> GREEN;
            case UNPAID -> RED;
            case OVERDUE -> OVERDUE_RED;
        };
        return new Font(Font.HELVETICA, 10, Font.BOLD, color);
    }

    private void addCell(PdfPTable table, String text, Font font) {
        addCell(table, text, font, null);
    }

    private void addCell(PdfPTable table, String text, Font font, Color backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        table.addCell(cell);
    }

    private void addGeneratedOn(Document document, String generatedOn, Font font) throws DocumentException {
        Paragraph generated = new Paragraph("Generated on: " + generatedOn, font);
        generated.setSpacingBefore(18);
        generated.setAlignment(Element.ALIGN_LEFT);
        document.add(generated);
    }

    private String value(Object value) {
        return value == null ? "Not available" : String.valueOf(value);
    }
}
