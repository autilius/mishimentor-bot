package org.example.brains;

import java.io.IOException;

public interface MishiBrain {
    String pensar(String peticion) throws IOException, InterruptedException;
    String getNombreModelo();
}