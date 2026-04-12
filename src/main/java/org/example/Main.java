package org.example;

import org.example.brains.GeminiBrain;
import org.example.brains.MishiBrain;
import org.example.brains.MockBrain;

import java.util.List;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;
import static org.example.MishiConsole.*;

public class Main {
    public static void main(String[] args) {
        MishiConsole ui = new MishiConsole();
        MishiVault vault = new MishiVault();
        vault.initVault();

        String apiKey = MishiConfig.getApiKey();
        String promptKey = MishiConfig.getSystemPrompt();
        MishiClient mishi = new MishiClient(apiKey, promptKey);

        MishiBrain cerebroActual;

        // El Mishi decide su nivel de inteligencia basado en su salud
        if (MishiHealth.hayInternet() && MishiHealth.tenemosCreditos()) {
            System.out.println(VERDE + "🐾 Mishi: Conectado a la red neuronal global." + RESET);
            cerebroActual = new GeminiBrain(apiKey, promptKey);
        } else {
            System.out.println(AMARILLO + "🐾 Mishi: Entrando en modo ahorro/local. (Sin internet o sin créditos)" + RESET);
            cerebroActual = new MockBrain();
        }

// Ahora, el resto del programa usa 'cerebroActual' sin importar cuál sea
        ui.escribirLento("Usando el cerebro: " + cerebroActual.getNombreModelo() + "\n", 30);

        ui.mostrarBienvenida();
        boolean ejecutando = true;

        while (ejecutando) {
            ui.mostrarMenu();
            String opcion = ui.pedirEntrada("Elige una opción");

            switch (opcion) {
                case "1" -> {
                    // 1. En lugar de escribir la ruta, abrimos la ventana vintage
                    String ruta = ui.seleccionarArchivoVintage();

                    if (ruta == null) {
                        ui.escribirLento("🐾 Mishi: ¿Te dio miedo? No elegiste nada... miau.\n", 30);
                    } else {
                        // 2. Si eligió un archivo, procedemos
                        ui.escribirLento("\n🐾 Mishi: Olfateando código en " + ruta + "\n", 50);

                        try {
                            // 3. El Scanner lee lo que la ventana consiguió
                            String codigo = MishiScanner.leerArchivo(ruta);

                            if (codigo.startsWith("¡Miau! Error")) {
                                System.err.println(codigo);
                            } else {
                                // 4. Enviamos a la IA
                                String respuesta = mishi.enviarMiau("Audita este código: \n" + codigo);

                                System.out.println("\n" + ui.pedirEntrada("Reporte generado. Presiona ENTER para leerlo"));
                                System.out.println(respuesta);

                                // 5. Guardar recuerdo en el baúl
                                MishiNode nodo = new MishiNode();
                                nodo.generarIdAutomatico();
                                nodo.setFileName(ruta); // Guardamos la ruta que nos dio la ventana
                                nodo.setVerdict(respuesta);
                                vault.guardarNodo(nodo);
                            }

                        } catch (Exception e) {
                            System.err.println("¡Miau! Algo salió mal: " + e.getMessage());
                        }
                    }
                }
                case "2" -> {
                    ui.escribirLento("🐾 Mishi: Abriendo el baúl de recuerdos... no juzgues lo que encuentres.\n", 30);
                    List<MishiNode> lista = vault.obtenerTodosLosRecuerdos();

                    if (lista.isEmpty()) {
                        System.out.println(ROJO + "El baúl está vacío. ¡Ve a trabajar un poco!" + RESET);
                    } else {
                        System.out.println("\n--- 📜 ARCHIVOS EN EL BAÚL ---");
                        for (int i = 0; i < lista.size(); i++) {
                            MishiNode n = lista.get(i);
                            System.out.printf("%d. [%s] - %s\n", i + 1, n.getId(), n.getFileName());
                        }

                        String respuesta = ui.pedirEntrada("Escribe el número para leer el reporte (o 0 para volver)");
                        try {
                            int index = Integer.parseInt(respuesta) - 1;
                            if (index >= 0 && index < lista.size()) {
                                MishiNode elegido = lista.get(index);
                                System.out.println("\n" + VERDE + "=== REPORTE GUARDADO EL " + elegido.getId() + " ===" + RESET);
                                System.out.println(elegido.getVerdict());
                                System.out.println(VERDE + "===============================================" + RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("¡Miau! Eso no es un número, humano.");
                        }
                    }
                }
                case "3" -> {
                    ui.escribirLento("🐾 Mishi: Al fin... miau. *Se enrosca y se duerme*\n", 50);
                    ejecutando = false;
                }
                default -> System.out.println("¿Esa opción qué? No tengo pulgares para eso.");
            }
        }
    }
}