package com.bugotruco.mishimentor.brains;

import java.io.IOException;

public interface MishiBrain {
    String pensar(String peticion) throws IOException, InterruptedException;
    String getNombreModelo();
}