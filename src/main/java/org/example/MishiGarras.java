package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MishiGarras {

    // Carpeta global de sugerencias
    private static final String MISHI_OUTPUT = "mishi_suggestions";

    public static void init() {
        try {
            Files.createDirectories(Paths.get(MISHI_OUTPUT));
        } catch (IOException e) {
            System.err.println("No se pudo inicializar la zona de salida.");
        }
    }

    /**
     * Escribe código mejorado para cualquier proyecto.
     * @param proyectoNombre Nombre de la subcarpeta (ej: "ProyectoWeb")
     * @param nombreArchivo Nombre del archivo (ej: "UsuarioService.java")
     * @param contenido El código generado por la IA
     */
    public static void exportarCodigo(String proyectoNombre, String nombreArchivo, String contenido) {
        try {
            Path directorioProyecto = Paths.get(MISHI_OUTPUT, proyectoNombre);
            Files.createDirectories(directorioProyecto);

            Path archivoFinal = directorioProyecto.resolve(nombreArchivo);
            Files.writeString(archivoFinal, contenido);

            System.out.println("🐾 Mishi: He dejado el código en: " + archivoFinal.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("¡Miau! No pude escribir el código externo: " + e.getMessage());
        }
    }
}