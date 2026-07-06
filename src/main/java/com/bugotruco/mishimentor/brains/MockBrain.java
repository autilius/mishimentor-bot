package com.bugotruco.mishimentor.brains;

import java.util.Random;

public class MockBrain implements MishiBrain {

    private final String[] respuestasGatunas = {
            "¡Miau! Estoy en modo ahorro de energía. El humano está cansado, así que solo diré: El código se ve aceptable.",
            "🙀 ¡ALERTA! Detecté una pulga gigante en tus bucles. Es broma, soy un Mock, pero limpia ese código por si las dudas.",
            "😼 Código aprobado por la PM Senior Mimi-chan. No encontré vulnerabilidades, pero me debes un sobre de salmón.",
            "🐾 [Modo Eco] Leyendo sintaxis... Todo limpio. El Mishi se va a dormir otra siesta."
    };

    @Override
    public String pensar(String peticion) {
        int index = new Random().nextInt(respuestasGatunas.length);
        return respuestasGatunas[index];
    }

    @Override
    public String getNombreModelo() {
        return "Modo Offline (Mishi Económico)";
    }
}