/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco.model;

/**
 * Representa un hallazgo específico de seguridad o arquitectura
 * detectado durante la auditoría.
 */
public class Finding {
    private String id;            // Ej: "SEC-001"
    private String title;         // Ej: "Uso de MD5 (Algoritmo Inseguro)"
    private String description;   // Ej: "El algoritmo MD5 es vulnerable a colisiones..."
    private String severity;      // HIGH, MEDIUM, LOW
    private String lineContext;   // Ej: "Línea 24: MessageDigest.getInstance(\"MD5\");"

    public Finding() {} // Constructor vacío obligatorio para Jackson

    // Constructor completo para cuando el orquestador cree los objetos
    public Finding(String id, String title, String description, String severity, String lineContext) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.lineContext = lineContext;
    }

    // --- Getters y Setters Estándar ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getLineContext() { return lineContext; }
    public void setLineContext(String lineContext) { this.lineContext = lineContext; }
}