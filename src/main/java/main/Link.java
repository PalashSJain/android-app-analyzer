package main;

import exceptions.IncompatibleURLException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Link {
    private String linkToRepo;
    private String linkToAPI;
    private String repoName, author;

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

    public synchronized String getReleasesFromGitHub() throws Exception {
        System.out.println("\nSending 'GET' request to URL : " + linkToAPI);
        URL url = new URL(linkToAPI);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "token " + Utils.getToken());
        con.setRequestProperty("User-Agent", Utils.getTokenAgent());

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

    public void formURLForAPI() throws IncompatibleURLException {
        if (author != null && repoName != null) {
            linkToAPI = "https://api.github.com/repos/" + author + "/" + repoName + "/releases";
        } else {
            throw new IncompatibleURLException("URL " + linkToRepo + " is not compatible.");
        }
    }

    public String getAPI() {
        return linkToAPI;
    }

    @Override
    public String toString(){
        return linkToRepo;
    }

    public String getRepoName() {
        return repoName;
    }

}
