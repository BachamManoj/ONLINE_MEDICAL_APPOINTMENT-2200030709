package com.klu.OnlineMedicalAppointment.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.OutputStream;

@RestController
public class PdfController {

    @GetMapping("/view-pdf")
    public void viewPdf(HttpServletResponse response) {
        try {
            // Set content type to PDF
            response.setContentType("application/pdf");

            // Prevent downloading; instead, render in the browser
            response.setHeader("Content-Disposition", "inline; filename=report.pdf");

            // Create a PDF document
            Document document = new Document();
            OutputStream out = response.getOutputStream();
            PdfWriter.getInstance(document, out);

            document.open();
            document.add(new Paragraph("Hello, this is a sample PDF rendered in the browser!"));
            document.close();

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
