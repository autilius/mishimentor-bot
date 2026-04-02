package org.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class MishiScanner {

    public static String leerArchivo(String rutaArchivo) {
        // toma gat
        try {
            return Files.readString(Path.of(rutaArchivo));
        } catch (IOException e) {
            return "¡Miau! No pude leer el archivo: " + e.getMessage();
        }
    }
}
