package main;

import android.Manifest;
import exceptions.DoesNotMeetMinCriteriaException;
import exceptions.IncompatibleURLException;
import github.ReleaseNotes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
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

    public void fetchReleases() throws IncompatibleURLException, IOException, ParseException, DoesNotMeetMinCriteriaException {
        releases = new ArrayList<>();

        link.formURLForAPI();
        String releaseInfo = link.getReleasesFromGitHub();
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(releaseInfo);

        if (array.size() < (Config.getMinNumberOfReleases() * Config.getDifferenceBetweenReleases())) {
            throw new DoesNotMeetMinCriteriaException("Number of releases for " + link.getRepoName() + " is " + array.size());
        }
        for (int i = 0; i < array.size(); i += Config.getDifferenceBetweenReleases()) {
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
            try {
                release.download();
                release.analyze();
                for (Manifest manifest : release.getManifests()) {
                    System.out.println(manifest.getMaxSDK() + " | " + manifest.getMinSDK() + " | " + manifest.getTargetSDK() + " | " + manifest.getPermissions());
                }
            } catch (IOException e) {
                System.out.println("Failed to download " + release.getPath());
                continue;
            } finally {
                if (!Config.isDebugModeOn()) release.delete();
                System.out.println("");
            }
        }
        if (!Config.isDebugModeOn()) deleteDownloadsFolder();
    }
}
