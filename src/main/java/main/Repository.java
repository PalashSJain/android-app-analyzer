package main;

import database.Database;
import exceptions.DoesNotMeetMinCriteriaException;
import exceptions.IncompatibleURLException;
import github.Issue;
import github.ReleaseNote;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.*;

/**
 * Created by Palash on 4/9/2017.
 */
public class Repository implements Comparable<Repository> {
    private List<Release> releases;
    private Link link;
    private String repoName;
    private Database db;
    private int id;

    public Repository(Link link) {
        this.link = link;
        this.repoName = this.link.getRepoName();
        db = Database.getInstance();
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

        String releaseInfo = link.getReleasesFromGitHub();
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(releaseInfo);

        if (array.size() < (Config.getMinNumberOfReleases() * Config.getDifferenceBetweenReleases())) {
            throw new DoesNotMeetMinCriteriaException("Number of releases for " + link.getRepoName() + " is " + array.size());
        }
        for (int i = 0; i < array.size(); i += Config.getDifferenceBetweenReleases()) {
            JSONObject json = (JSONObject) array.get(i);

            ReleaseNote notes = new ReleaseNote();
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
                release.setId(db.addRelease(getId()));
                release.addReleaseNoteToDB();
                release.scanLibraries();
                release.scanFiles();
            } catch (IOException e) {
                System.out.println("Failed to download " + release.getPath());
                e.printStackTrace();
                continue;
            } finally {
                if (!Config.isDebugModeOn()) release.delete();
                System.out.println("");
            }
        }
        if (!Config.isDebugModeOn()) deleteDownloadsFolder();
    }

    private Stack<Issue> parseIssues(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(jsonString);
        Stack<Issue> issues = new Stack<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject json = (JSONObject) array.get(i);
            Issue issue = new Issue();
            String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            try {
                String d = (String) json.get("closed_at");
                Calendar instance = Calendar.getInstance();
                if (d != null) {
                    Date date = new Date(new SimpleDateFormat(pattern).parse(d).getTime());
                    issue.setClosedAt(date);
                }

                Date date = new Date(new SimpleDateFormat(pattern).parse((String) json.get("updated_at")).getTime());
                issue.setUpdatedAt(date);

                date = new Date(new SimpleDateFormat(pattern).parse((String) json.get("created_at")).getTime());
                issue.setCreatedAt(date);

                issue.setState((String) json.get("state"));
                issues.push(issue);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        return issues;
    }

    public synchronized void analyzeIssues() {
        try {
            Stack<Issue> allIssues = parseIssues(link.lookUpGitHubIssues());
            for (int i = releases.size() - 1; i >= 0; i--) {
                Release release = releases.get(i);
                Stack<Issue> issues = new Stack<>();
                for (int j = allIssues.size() - 1; j >= 0; j--) {
                    Issue issue = allIssues.pop();
                    if (issue.getCreatedAt().compareTo(release.getReleaseNotes().getPublishedAt()) > 0) {
                        db.addIssues(release.getId(), issue);
                        issues.push(issue);
                    } else {
                        break;
                    }
                }
                release.setIssues(issues);
            }
        } catch (IOException | IncompatibleURLException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getRepoName() {
        return repoName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}