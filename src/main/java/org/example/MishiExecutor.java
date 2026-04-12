package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MishiExecutor {

    // Directorio donde los usuarios pondrán sus scripts
    private static final String SCRIPTS_DIR = "mishi_scripts";

    /**
     * Ejecuta un script externo y le pasa el código auditado.
     * @param nombreScript Ejemplo: "limpiar_comentarios.py" o "analisis.js"
     * @param contenidoArchivo El código que queremos procesar
     */
    public static String ejecutarScriptExterno(String nombreScript, String contenidoArchivo) {
        StringBuilder output = new StringBuilder();
        try {
            // Determinamos el comando según la extensión
            List<String> comando = new ArrayList<>();
            if (nombreScript.endsWith(".py")) comando.add("python3");
            else if (nombreScript.endsWith(".js")) comando.add("node");
            else if (nombreScript.endsWith(".sh")) comando.add("bash");

            comando.add(SCRIPTS_DIR + "/" + nombreScript);

            ProcessBuilder pb = new ProcessBuilder(comando);
            Process proceso = pb.start();

            // Enviamos el contenido del archivo al script a través de la entrada estándar (stdin)
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(proceso.getOutputStream()))) {
                writer.write(contenidoArchivo);
            }

            // Leemos la respuesta del script (stdout)
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    output.append(linea).append("\n");
                }
            }

            proceso.waitFor();
        } catch (Exception e) {
            return "¡Miau! Falló el script externo: " + e.getMessage();
        }
        return output.toString();
    }
}