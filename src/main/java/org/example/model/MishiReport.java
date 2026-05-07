package org.example.model;

import java.util.List;

/**
 * Versión Refactorizada 2.0
 * Eliminamos duplicados para mantener el código limpio.
 */
public class MishiReport {
    private String fileName;
    private String date; // En lugar de timestamp
    private List<Finding> securityFindings;
    private String refactoredCode; // En lugar de cleanCode

    // Constructor vacío (Obligatorio para Jackson)
    public MishiReport() {}

    // Constructor para inicialización rápida
    public MishiReport(String fileName, String date, String refactoredCode) {
        this.fileName = fileName;
        this.date = date;
        this.refactoredCode = refactoredCode;
    }

    // --- GETTERS Y SETTERS ---

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<Finding> getSecurityFindings() { return securityFindings; }
    public void setSecurityFindings(List<Finding> securityFindings) { this.securityFindings = securityFindings; }

    public String getRefactoredCode() { return refactoredCode; }
    public void setRefactoredCode(String refactoredCode) { this.refactoredCode = refactoredCode; }
}