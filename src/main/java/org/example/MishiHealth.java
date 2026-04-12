package org.example;

import java.net.InetAddress;

public class MishiHealth {

    /**
     * Intenta hacer un "ping" rápido a los servidores de Google (8.8.8.8)
     * para ver si el humano tiene internet.
     */
    public static boolean hayInternet() {
        try {
            // Tiempo de espera de 2 segundos
            return InetAddress.getByName("8.8.8.8").isReachable(2000);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Aquí podríamos conectar con una base de datos local o archivo
     * para verificar si nos quedan tokens en las APIs.
     * Por ahora, lo dejamos como un "semáforo" manual.
     */
    public static boolean tenemosCreditos() {
        // En el futuro, esto leerá un contador de un archivo JSON
        return true;
    }
}
