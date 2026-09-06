package com.bugotruco.mishimentor.git;

import java.io.File;
import java.util.List;

public class MishiGitFacade {

    private final MishiGitProvider gitProvider;

    public MishiGitFacade() {
        this.gitProvider = new GenericGitService();
    }

    public List<File> prepareRepoForAudit(String repoUrl, String token, MishiGitProvider.CloneMode mode, String branchOrCommit) throws Exception {
        String platform = gitProvider.getPlatformName(repoUrl);
        System.out.println("🛰️ [MishiGit] Plataforma detectada: " + platform);

        File baseVault = new File(System.getProperty("user.home"), ".mishi_vault/workspace");
        File tempRepoDir = new File(baseVault, "repo_" + System.currentTimeMillis());

        if (!tempRepoDir.exists() && !tempRepoDir.mkdirs()) {
            throw new IllegalStateException("No se pudo crear el workspace en: " + tempRepoDir.getAbsolutePath());
        }

        gitProvider.cloneRepository(repoUrl, tempRepoDir, token, mode, branchOrCommit);
        return gitProvider.extractJavaSourceFiles(tempRepoDir);
    }

    public List<String> inspectCommits(String repoUrl, String token, int limit) throws Exception {
        return gitProvider.fetchRecentCommits(repoUrl, token, limit);
    }
}