package org.example;

import org.example.brains.MishiBrain;
import org.example.search.MishiSeeker;

import java.util.List;

public class MishiOrchestrator {
    private MishiBrain brain;
    private final MishiVault vault;
    private MishiSeeker seeker; // Nueva herramienta

    public MishiOrchestrator(MishiVault vault, MishiBrain brain) {
        this.vault = vault;
        this.brain = brain;
    }

    /**
     * Flujo Maestro:
     * 1. La IA analiza el código.
     * 2. Un script externo (opcional) refina el resultado.
     * 3. Se guarda en el Vault.
     */
    public void procesarAuditoriaCompleta(String rutaArchivo, String nombreScriptOpcional) {
        try {
            // 1. Leer el código
            String codigoOriginal = MishiScanner.leerArchivo(rutaArchivo);

            // 2. Pensar con la IA
            System.out.println("🐾 Mishi: Consultando a la inteligencia colectiva...");
            String veredictoIA = brain.pensar("Audita este código: \n" + codigoOriginal);

            // 3. ¿Hay un script externo para mejorar esto?
            String resultadoFinal = veredictoIA;
            if (nombreScriptOpcional != null && !nombreScriptOpcional.isEmpty()) {
                System.out.println("🐾 Mishi: Pasando reporte por el script: " + nombreScriptOpcional);
                resultadoFinal = MishiExecutor.ejecutarScriptExterno(nombreScriptOpcional, veredictoIA);
            }

            // 4. Guardar y Mostrar
            System.out.println("\n--- RESULTADO FINAL ---\n" + resultadoFinal);

            MishiNode nodo = new MishiNode();
            nodo.setFileName(rutaArchivo);
            nodo.setVerdict(resultadoFinal);
            vault.guardarNodo(nodo);

        } catch (Exception e) {
            System.err.println("¡Miau! El orquestador falló: " + e.getMessage());
        }
    }

    public void auditoriaConBusqueda(String rutaArchivo) {
        try {
            String codigo = MishiScanner.leerArchivo(rutaArchivo);

            // 1. El Mishi decide qué buscar (Opcional)
            List<String> hallazgosWeb = seeker.buscar("Vulnerabilidades comunes en: " + rutaArchivo);

            // 2. Le pasamos el código Y la información de la web a la IA
            String contexto = "Código:\n" + codigo + "\n\nInfo Web Actualizada:\n" + hallazgosWeb;
            String veredicto = brain.pensar("Audita usando esta info fresca:\n" + contexto);

            // 3. Guardar en el baúl
            // ... (lo que ya teníamos)

        } catch (Exception e) {
            System.err.println("¡Miau! Error en búsqueda: " + e.getMessage());
        }
    }
}