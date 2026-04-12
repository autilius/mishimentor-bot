package org.example.brains;

public class MockBrain implements MishiBrain {
    @Override
    public String pensar(String peticion) {
        return "¡Miau! Estoy en modo ahorro de energía. El humano está enfermo, así que solo diré: El código se ve aceptable.";
    }

    @Override
    public String getNombreModelo() {
        return "Modo Offline (Mishi Económico)";
    }
}