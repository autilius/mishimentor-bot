package org.example;

import org.example.model.Finding; // Importamos tu clase Finding
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MishiNode {
    private String id;
    private String parentId;
    private String fileName;
    private String verdict;      // Aquí guardamos el consejo/explicación del Mishi
    private String refactoredCode; // <-- NUEVO: El código limpio
    private List<Finding> securityFindings = new ArrayList<>(); // <-- NUEVO: La lista de errores
    private long timestamp;

    public MishiNode() {} // Para Jackson

    public MishiNode(String parentId, String fileName, String verdict, String refactoredCode) {
        generarIdAutomatico();
        this.parentId = parentId;
        this.fileName = fileName;
        this.verdict = verdict;
        this.refactoredCode = refactoredCode;
        this.timestamp = System.currentTimeMillis();
    }

    // --- MÉTODOS NUEVOS Y CORREGIDOS ---

    public void generarIdAutomatico() {
        this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    public String getRefactoredCode() {
        return refactoredCode;
    }

    public void setRefactoredCode(String refactoredCode) {
        this.refactoredCode = refactoredCode;
    }

    public List<Finding> getSecurityFindings() {
        return securityFindings;
    }

    public void setSecurityFindings(List<Finding> securityFindings) {
        this.securityFindings = securityFindings;
    }

    // --- GETTERS Y SETTERS CLÁSICOS ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getVerdict() { return verdict; }
    public void setVerdict(String verdict) { this.verdict = verdict; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}