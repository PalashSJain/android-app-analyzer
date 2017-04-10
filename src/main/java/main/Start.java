package main;

import exceptions.IncompatibleURLException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Palash on 4/9/2017.
 */
public class Start {
    private List<Repository> repositories;
    private CSVHandler csvHandler;

    public Start() {
        Utils.initialize();
        csvHandler = new CSVHandler("/app_source.csv");
        repositories = new ArrayList<>();
    }

    public static void main(String[] args) {
        Start app = new Start();
        try {
            app.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() throws IOException {
        csvHandler.parse();
        Set<Link> links = csvHandler.getLinks();

        getRelevantRepositories(links);

        Collections.sort(repositories);
        csvHandler.write(repositories);

        extractRepositories();
    }

    private void extractRepositories() {
        for (Repository repository : repositories) {
            repository.createDownloadsFolder();
            List<Release> releases = repository.getReleases();
            for (Release release : releases) {
                analyzeRelease(release);
            }
            if (!Utils.isDebugModeOn()) repository.deleteDownloadsFolder();
        }
    }

    private void analyzeRelease(Release release) {
        try {
            release.download();
        } catch (IOException e) {
            System.out.println("Failed to download " + release.getName() + ". Going to next release.");
            return;
        }
        release.parseManifest();
        release.scanForIssues();
        if (!Utils.isDebugModeOn()) release.delete();
        System.out.println("");
    }

    private void getRelevantRepositories(Set<Link> links) {
        for (Link link : links) {
            Repository repo;
            try {
                repo = new Repository(link);
                repo.fetchReleases();
                repositories.add(repo);
                if (repositories.size() == 15) break;
            } catch (Exception e) {
                System.out.println("Link " + link.get() + " is not eligible.");
            }
        }
    }

}
