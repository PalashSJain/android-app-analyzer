package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Start {
    private List<Repository> repositories;
    private CSVHandler csvHandler;

    public Start() {
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
        List<Link> links = csvHandler.getLinks();

        for (Link link : links) {
            List<Release> releases = link.getReleases();
            csvHandler.write("out.csv", new String[]{link.get(), String.valueOf(releases.size())});
            repositories.add(new Repository(releases));
        }
        Collections.sort(repositories);
        for (Repository repository : repositories) {
            List<Release> releases = repository.getReleases();
            for (Release release : releases) {
                release.download();
                release.parseManifest();
                release.scanForIssues();
                release.delete();
            }
        }
    }

}
