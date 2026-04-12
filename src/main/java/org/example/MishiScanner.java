package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MishiScanner {

    // Lee un solo archivo (lo que ya teníamos)
    public static String leerArchivo(String ruta) throws IOException {
        return Files.readString(Paths.get(ruta));
    }

    // NUEVO: Lee todo un proyecto y lo empaqueta para la IA
    public static String leerProyectoCompleto(String rutaRaiz) {
        try (Stream<Path> paths = Files.walk(Paths.get(rutaRaiz))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(path -> {
                        try {
                            return "--- ARCHIVO: " + path.getFileName() + " ---\n" + Files.readString(path);
                        } catch (IOException e) {
                            return "Error leyendo: " + path.getFileName();
                        }
                    })
                    .collect(Collectors.joining("\n\n"));
        } catch (IOException e) {
            return "¡Miau! No pude entrar a esa carpeta: " + e.getMessage();
        }
    }
}