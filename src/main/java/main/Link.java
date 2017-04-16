package main;

import exceptions.IncompatibleURLException;
import github.Issue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Link {
    private String linkToRepo;
    private String linkToReleasesAPI;
    private String repoName, author;
    private String linkToIssuesAPI;

    public Link(String link) {
        this.linkToRepo = link;
        breakLink();
    }

    private void breakLink() {
        if (linkToRepo.startsWith("https://github.com/") || linkToRepo.startsWith("https://gitlab.com/")) {
            String temp = linkToRepo.replace(".git", "");
            String[] splits = temp.split("/");
            if (splits.length >= 5){
                author = splits[3];
                repoName = splits[4];
            }
        }
    }

    public String get() {
        return linkToRepo;
    }

    public synchronized String getReleasesFromGitHub() throws IOException, IncompatibleURLException {
        formURLForReleases();
        System.out.println("\nSending 'GET' request to URL : " + linkToReleasesAPI);
        URL url = new URL(linkToReleasesAPI);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "token " + Config.getToken());
        con.setRequestProperty("User-Agent", Config.getTokenAgent());

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private void formURLForReleases() throws IncompatibleURLException {
        if (author != null && repoName != null) {
            linkToReleasesAPI = getAPI() + "/releases";
        } else {
            throw new IncompatibleURLException("URL " + linkToRepo + " is not compatible.");
        }
    }

    private void formURLForIssues() throws IncompatibleURLException {
        if (author != null && repoName != null) {
            linkToIssuesAPI = getAPI() + "/issues?state=all&sort=created&direction=asc";
        } else {
            throw new IncompatibleURLException("URL " + linkToRepo + " is not compatible.");
        }
    }

    private String getAPI() {
        return "https://api.github.com/repos/" + author + "/" + repoName;
    }

    @Override
    public String toString(){
        return linkToRepo;
    }

    public String getRepoName() {
        return repoName;
    }

    public String lookUpGitHubIssues() throws IOException, IncompatibleURLException {
        formURLForIssues();
        System.out.println("\nSending 'GET' request to URL : " + linkToIssuesAPI);
        URL url = new URL(linkToIssuesAPI);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "token " + Config.getToken());
        con.setRequestProperty("User-Agent", Config.getTokenAgent());

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
