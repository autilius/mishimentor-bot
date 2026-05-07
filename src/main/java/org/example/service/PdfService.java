package org.example.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.example.model.Finding;
import org.example.model.MishiReport;

import java.io.FileOutputStream;

public class PdfService {

    public void convertJsonToPdf(MishiReport report, String outputPath) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // 1. Título
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            document.add(new Paragraph("Mishi Mentor: Reporte de Auditoría", fontTitulo));

            // 2. Metadatos
            document.add(new Paragraph("Archivo: " + report.getFileName()));
            document.add(new Paragraph("Fecha: " + report.getDate()));
            document.add(new Paragraph("\n"));

            // 3. SECCIÓN DE HALLAZGOS (Lo nuevo y pro)
            document.add(new Paragraph("--- Hallazgos de Seguridad ---"));
            for (Finding f : report.getSecurityFindings()) {
                document.add(new Paragraph("• [" + f.getSeverity() + "] " + f.getDescription()));
            }

            document.add(new Paragraph("\n--- Código Refactorizado ---\n"));

            // 4. Código Limpio
            Font fontCodigo = FontFactory.getFont(FontFactory.COURIER, 10);
            document.add(new Paragraph(report.getRefactoredCode(), fontCodigo));

            document.close();
            System.out.println("🐈‍⬛ ¡Miau! Reporte de Gala generado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}