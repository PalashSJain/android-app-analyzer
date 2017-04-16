package main;

import exceptions.DoesNotMeetMinCriteriaException;
import exceptions.IncompatibleURLException;
import github.Issue;
import org.json.simple.parser.ParseException;

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
        Config.initialize();
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
        for (Repository repository : repositories) {
            //repository.analyzeReleases();
            repository.analyzeIssues();
        }
        if (!Config.isDebugModeOn()) Utils.deleteDownloads();

    }

    private List<Repository> getRelevantRepositories(Set<Link> links) {
        List<Repository> repositories = new ArrayList<>();
        if (Config.getMaxRepositories() != 0) {
            for (Link link : links) {
                Repository repo;
                try {
                    repo = new Repository(link);
                    repo.fetchReleases();
                    repositories.add(repo);
                    if (repositories.size() == Config.getMaxRepositories()) break;
                } catch (IncompatibleURLException | DoesNotMeetMinCriteriaException e) {
                    System.out.println(e.getMessage());
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return repositories;
    }

}
