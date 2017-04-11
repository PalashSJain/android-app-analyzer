package main;

import exceptions.IncompatibleURLException;
import github.ReleaseNotes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Repository implements Comparable<Repository> {
    private List<Release> releases;
    private Link link;

    public Repository(Link link) {
        this.link = link;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

    @Override
    public int compareTo(Repository o) {
        return o.getReleases().size() - this.getReleases().size();
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public void fetchReleases() throws IncompatibleURLException {
        releases = new ArrayList<>();

        link.formURLForAPI();
        try {
            String releaseInfo = link.getReleasesFromGitHub();
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(releaseInfo);
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = (JSONObject) array.get(i);

                ReleaseNotes notes = new ReleaseNotes();
                notes.setDownloadURL((String) json.get("zipball_url"));
                notes.setName((String) json.get("name"));
                notes.setTagName((String) json.get("tag_name"));
                notes.setCreatedAt((String) json.get("created_at"));
                notes.setPublishedAt((String) json.get("published_at"));

                Release release = new Release();
                release.setRepo(link.getRepoName());
                release.setReleaseNotes(notes);

                releases.add(release);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void createDownloadsFolder() {
        Utils.createInDownloadFolder(link.getRepoName());
    }

    public void deleteDownloadsFolder() {
        Utils.deleteFromDownloadFolder(link.getRepoName());
    }

    public void analyzeReleases() {
        createDownloadsFolder();
        List<Release> releases = getReleases();
        for (Release release : releases) {
            release.analyze();
        }
        if (!Utils.isDebugModeOn()) deleteDownloadsFolder();
    }
}
