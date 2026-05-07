package org.example;

import org.example.brains.GeminiBrain;
import org.example.brains.MishiBrain;
import org.example.brains.MockBrain;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Simplemente encendemos la consola
        MishiConsole console = new MishiConsole();
        console.iniciar();
    }
}