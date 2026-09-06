package com.bugotruco.mishimentor.git;

import java.io.File;
import java.util.List;

public interface MishiGitProvider {

    enum CloneMode {
        SHALLOW, // Profundidad 1 (Rápido, solo último commit)
        FULL     // Historial completo
    }

    /**
     * Clona un repositorio remoto soportando modo Shallow o Full, y una rama/commit opcional.
     */
    File cloneRepository(String repoUrl, File targetDir, String token, CloneMode mode, String branchOrCommit) throws Exception;

    /**
     * Consulta el historial reciente de commits sin descargar el árbol de archivos completo.
     */
    List<String> fetchRecentCommits(String repoUrl, String token, int limit) throws Exception;

    List<File> extractJavaSourceFiles(File repoDir);

    String getPlatformName(String repoUrl);
}