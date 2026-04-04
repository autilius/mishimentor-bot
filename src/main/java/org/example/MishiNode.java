package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MishiNode {
    public String id;           // Ej: 20260402_1530
    public String parentId;     // El "Hilo Rojo" al nodo anterior
    public String fileName;     // Qué archivo auditamos
    public String verdict;      // El consejo del Mishi
    public long timestamp;

    public MishiNode() {} // Para Jackson

    public MishiNode(String id, String parentId, String fileName, String verdict) {
        this.id = id;
        this.parentId = parentId;
        this.fileName = fileName;
        this.verdict = verdict;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void generarIdAutomatico() {
        this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MishiNode{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", verdict='" + verdict + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
