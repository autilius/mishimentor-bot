package org.example.search;

import java.util.List;
import java.util.ArrayList;

public class TavilySearch implements MishiSeeker {
    private final String apiKey;

    public TavilySearch(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<String> buscar(String consulta) {
        // Aquí iría tu petición HTTP a Tavily o Google
        System.out.println("🔍 Mishi: Rastreando la web para: " + consulta);

        // Simulación por ahora (Mock)
        return List.of(
                "Resultado 1: La vulnerabilidad CVE-2026-XXXX fue parcheada en Java 17.0.12",
                "Resultado 2: Mejores prácticas de Clean Code para Records en Java."
        );
    }
}