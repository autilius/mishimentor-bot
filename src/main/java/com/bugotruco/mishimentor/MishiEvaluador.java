package com.bugotruco.mishimentor;

import java.util.regex.Pattern;

public class MishiEvaluador {

    public static void evaluarYMostrarResultados(String reporteIA) {
        if (reporteIA == null || reporteIA.isBlank()) return;

        // Convertimos a minúsculas para un parseo más rápido y sin problemas de Case
        String texto = reporteIA.toLowerCase();

        // Contadores de severidad basados en el reporte de la IA
        int criticos = contarCoincidencias(texto, "vulnerabilidad|critico|critical|sql injection|xss|owasp|seguridad");
        int advertencias = contarCoincidencias(texto, "advertencia|warning|bad practice|mala practica|hardcoded");
        int mejoras = contarCoincidencias(texto, "optimizacion|clean code|refactor|mejorar|comentario");

        // Algoritmo simple de Mishi Puntos (Empezamos en 10 y restamos peso)
        int score = 10 - (criticos * 3) - (advertencias * 1) - (mejoras * 0);
        if (score < 0) score = 0; // El código no puede ser más bajo que cero pulgas

        // Desplegamos el veredicto visual en la MishiConsole
        imprimirVeredicto(score, criticos, advertencias);
    }

    private static int contarCoincidencias(String texto, String regex) {
        var matcher = Pattern.compile(regex).matcher(texto);
        int coincidencias = 0;
        while (matcher.find()) {
            coincidencias++;
        }
        return coincidencias;
    }

    private static void imprimirVeredicto(int score, int criticos, int advertencias) {
        System.out.println("\n🐾 ================================================== 🐾");
        System.out.println("            📊 EL MISHIÓMETRO DE SEGURIDAD v3.0         ");
        System.out.println("🐾 ================================================== 🐾");

        if (score >= 9) {
            System.out.println("  😺 [Mishiómetro: " + score + "/10] - ¡Miau purrfecto!");
            System.out.println("  -> Tu código está más limpio que los bigotes de Mimi-chan.");
        } else if (score >= 6) {
            System.out.println("  😼 [Mishiómetro: " + score + "/10] - Código Aceptable");
            System.out.println("  -> Hay detalles (" + advertencias + " advertencias). Kakashi-kun te vigila de reojo.");
        } else {
            System.out.println("  🙀 [Mishiómetro: " + score + "/10] - ¡KERNEL PANIC GATUNO!");
            System.out.println("  -> ¡Alerta! Detectamos " + criticos + " fallos críticos. ¡Ese código tiene más pulgas que un callejero!");
        }
        System.out.println("🐾 ==================================================\n");
    }
}