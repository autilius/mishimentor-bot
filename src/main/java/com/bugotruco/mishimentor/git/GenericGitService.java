package com.bugotruco.mishimentor.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenericGitService implements MishiGitProvider {

    @Override
    public File cloneRepository(String repoUrl, File targetDir, String token, CloneMode mode, String branchOrCommit) throws Exception {
        System.out.println("🌐 [MishiGit] Iniciando clonado táctico (" + mode + ") desde: " + repoUrl);

        var cloneCommand = Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(targetDir);

        if (token != null && !token.isBlank()) {
            cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("oauth2", token));
        }

        if (mode == CloneMode.SHALLOW) {
            cloneCommand.setDepth(1);
            cloneCommand.setCloneAllBranches(false);
        } else {
            cloneCommand.setCloneAllBranches(true);
        }

        if (branchOrCommit != null && !branchOrCommit.isBlank() && !branchOrCommit.matches("^[0-9a-f]{7,40}$")) {
            cloneCommand.setBranch(branchOrCommit);
        }

        try (Git git = cloneCommand.call()) {
            // Si es Full Clone y se especificó un Hash de commit exacto
            if (mode == CloneMode.FULL && branchOrCommit != null && branchOrCommit.matches("^[0-9a-f]{7,40}$")) {
                git.checkout().setName(branchOrCommit).call();
                System.out.println("🎯 [MishiGit] Posicionado en el Commit específico: " + branchOrCommit);
            }

            System.out.println("✅ [MishiGit] Repositorio clonado exitosamente en: " + targetDir.getAbsolutePath());
            return targetDir;
        }
    }

    @Override
    public List<String> fetchRecentCommits(String repoUrl, String token, int limit) throws Exception {
        List<String> commitLogs = new ArrayList<>();
        System.out.println("🔍 [MishiGit] Consultando historial remoto de commits...");

        File tempMetaDir = Files.createTempDirectory("mishi_meta_").toFile();
        tempMetaDir.deleteOnExit();

        var cloneCommand = Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(tempMetaDir)
                .setNoCheckout(true);

        if (token != null && !token.isBlank()) {
            cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("oauth2", token));
        }

        try (Git git = cloneCommand.call()) {
            Repository repo = git.getRepository();
            ObjectId head = repo.resolve(Constants.HEAD);

            if (head != null) {
                try (RevWalk walk = new RevWalk(repo)) {
                    RevCommit commit = walk.parseCommit(head);
                    walk.markStart(commit);
                    int count = 0;
                    for (RevCommit rev : walk) {
                        if (count >= limit) break;
                        String shortHash = rev.getId().name().substring(0, 7);
                        String msg = rev.getShortMessage();
                        String author = rev.getAuthorIdent().getName();
                        commitLogs.add(String.format("[%s] %s (por %s)", shortHash, msg, author));
                        count++;
                    }
                }
            }
        }
        return commitLogs;
    }

    @Override
    public List<File> extractJavaSourceFiles(File repoDir) {
        List<File> javaFiles = new ArrayList<>();
        if (repoDir == null || !repoDir.exists()) return javaFiles;

        try (Stream<java.nio.file.Path> walk = Files.walk(repoDir.toPath())) {
            javaFiles = walk
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(java.nio.file.Path::toFile)
                    .collect(Collectors.toList());

            System.out.println("🔍 [MishiGit] Archivos .java detectados: " + javaFiles.size());
        } catch (IOException e) {
            System.err.println("❌ [MishiGit] Error al escanear directorio: " + e.getMessage());
        }
        return javaFiles;
    }

    @Override
    public String getPlatformName(String repoUrl) {
        if (repoUrl == null) return "Desconocida";
        String lower = repoUrl.toLowerCase();

        if (lower.contains("github.com")) return "GitHub";
        if (lower.contains("gitlab.com") || lower.contains("gitlab")) return "GitLab";
        if (lower.contains("bitbucket.org")) return "Bitbucket";
        if (lower.contains("azure.com") || lower.contains("visualstudio.com")) return "Azure DevOps";

        return "Servidor Git Genérico / Self-Hosted";
    }
}