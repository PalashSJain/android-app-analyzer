package main;

import exceptions.IncompatibleURLException;
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
                Release release = new Release();
                release.setRepo(link.getRepoName());
                JSONObject json = (JSONObject) array.get(i);
                release.setDownloadURL((String) json.get("zipball_url"));
                release.setName((String) json.get("name"));
                release.setTagName((String) json.get("tag_name"));
                release.setCreatedAt((String) json.get("created_at"));
                release.setPublishedAt((String) json.get("published_at"));
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
}
