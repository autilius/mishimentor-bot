package org.example.model;

public class Finding {
    private String id;          // Un identificador único (ej: "SEC-001")
    private String description; // Qué encontramos (ej: "Variable expuesta")
    private String severity;    // HIGH, MEDIUM, LOW

    public Finding() {} // Para Jackson

    public Finding(String id, String description, String severity) {
        this.id = id;
        this.description = description;
        this.severity = severity;
    }

    // --- Getters y Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
}