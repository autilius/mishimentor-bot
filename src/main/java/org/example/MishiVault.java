package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MishiVault {

    private static final String VAULT_NAME = ".mishi_vault";
    private final ObjectMapper mapper;

    public MishiVault() {
        this.mapper = new ObjectMapper();
        // Esto hace que el JSON se guarde "bonito" (con sangría)
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Path getVaultPath() {
        return Paths.get(System.getProperty("user.home"), VAULT_NAME);
    }

    public void initVault() {
        try {
            Path ruta = getVaultPath();
            if (Files.notExists(ruta)) {
                Files.createDirectory(ruta);
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar el baúl: " + e.getMessage());
        }
    }

    // --- EL CORAZÓN DE LA MEMORIA ---
    public void guardarNodo(MishiNode nodo) {
        // 1. Definir el nombre del archivo (usando el ID del nodo)
        String nombreArchivo = nodo.getId() + ".json";
        Path rutaArchivo = getVaultPath().resolve(nombreArchivo);

        try {
            // 2. Convertir el objeto MishiNode a un String JSON
            String jsonGatuno = mapper.writeValueAsString(nodo);

            // 3. Escribir el archivo (Java 11+ permite hacerlo en una línea)
            Files.writeString(rutaArchivo, jsonGatuno);

            System.out.println("🐾 Mishi: Recuerdo guardado con éxito en: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("¡Miau! Se me escapó el ovillo al guardar: " + e.getMessage());
        }
    }

    public List<MishiNode> obtenerTodosLosRecuerdos() {
        List<MishiNode> recuerdos = new ArrayList<>();

        try (Stream<Path> archivos = Files.list(getVaultPath())) {
            archivos.filter(p -> p.toString().endsWith(".json"))
                    .forEach(p -> {
                        try {
                            // Jackson hace la magia inversa: Archivo -> Objeto
                            MishiNode nodo = mapper.readValue(p.toFile(), MishiNode.class);
                            recuerdos.add(nodo);
                        } catch (IOException e) {
                            System.err.println("¡Miau! Ignorando archivo corrupto: " + p.getFileName());
                        }
                    });
        } catch (IOException e) {
            System.err.println("No pude abrir el baúl: " + e.getMessage());
        }

        // Los ordenamos por fecha (el más nuevo primero)
        recuerdos.sort((n1, n2) -> Long.compare(n2.getTimestamp(), n1.getTimestamp()));
        return recuerdos;
    }
}