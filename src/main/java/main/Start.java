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
    private CSVHandler csvHandler;

    public Start() {
        Utils.initialize();
        csvHandler = new CSVHandler("/app_source.csv");
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

        List<Repository> repositories = getRelevantRepositories(links);

        Collections.sort(repositories);
        csvHandler.write(repositories);

        Utils.createDownloads();
        analyzeRepositories(repositories);
        if(!Utils.isDebugModeOn()) Utils.deleteDownloads();
    }

    private void analyzeRepositories(List<Repository> repositories) {
        for (Repository repository : repositories) {
            repository.analyzeReleases();
        }
    }

    private List<Repository> getRelevantRepositories(Set<Link> links) {
        List<Repository> repositories = new ArrayList<>();
        for (Link link : links) {
            Repository repo;
            try {
                repo = new Repository(link);
                repo.fetchReleases();
                repositories.add(repo);
                if (repositories.size() == 3) break;
            } catch (IncompatibleURLException e) {
                e.printStackTrace();
            }
        }
        return repositories;
    }

}
