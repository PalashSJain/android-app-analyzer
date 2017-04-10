package main;

import exceptions.IncompatibleURLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Link {
    private List<Release> releases;
    private String linkToRepo;
    private String linkToAPI;

    public Link(String link) {
        this.linkToRepo = link;
    }

    public List<Release> getReleases() throws IncompatibleURLException {
        releases = new ArrayList<>();

        formURLForAPI();
        try {
            String releaseInfo = getReleasesFromGitHub();
            JSONParser parser = new JSONParser();
            JSONArray object = (JSONArray) parser.parse(releaseInfo);
            for (int i = 0; i < object.size(); i++) {
                Release release = new Release();
                JSONObject json = (JSONObject) object.get(i);
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
        return releases;
    }

    public String get() {
        return linkToRepo;
    }

    private synchronized String getReleasesFromGitHub() throws Exception {
        System.out.println("\nSending 'GET' request to URL : " + linkToAPI);
        URL url = new URL(linkToAPI);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "token 481ef9a17413bfcd2ad1e68943744833cb68f457");
        con.setRequestProperty("User-Agent", "PalashSJain");

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

    private void formURLForAPI() throws IncompatibleURLException {
        // linkToAPI  -- https://api.github.com/repos/andraus/BluetoothHidEmu/releases
        // linkToRepo -- https://github.com/andraus/BluetoothHidEmu
        if (linkToRepo.startsWith("https://github.com/") || linkToRepo.startsWith("https://gitlab.com/")) {
            linkToRepo = linkToRepo.replace(".git", "");
            String[] parts = linkToRepo.split("/");
            try {
                linkToAPI = "https://api.github.com/repos/" + parts[3] + "/" + parts[4] + "/releases";
            } catch (Exception e) {
                throw new IncompatibleURLException("URL " + linkToRepo + " is not compatible.");
            }
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
}
