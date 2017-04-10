package main;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Release {
    private String path;
    private String zip;
    private int minSDK;
    private int maxSDK;
    private List<AndroidPermission> permissions;
    private List<Library> safeLibraries;
    private List<Library> vulnLibraries;
    private String downloadURL;
    private String name;
    private String tagName;
    private Date createdAt;
    private Date publishedAt;
    private String repo;

    public Release(){
    }

    public int getMinSDK() {
        return minSDK;
    }

    public int getMaxSDK() {
        return maxSDK;
    }

    public List<AndroidPermission> getPermissions() {
        return permissions;
    }

    public List<Library> getSafeLibraries() {
        return safeLibraries;
    }

    public List<Library> getVulnLibraries() {
        return vulnLibraries;
    }

    public void delete() {
        System.out.println("Deleting " + path);
        if (Utils.deleteFromDownloadFolder(path)){
            System.out.println("Deleted " + path);
        } else {
            System.out.println("Failed to delete " + path);
        }
    }

    public void parseManifest() {

    }

    public synchronized void download() throws IOException {
        zip = tagName + ".zip";
        path = repo + "/" + zip;
        URL url = new URL(getDownloadURL());
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        try (InputStream stream = con.getInputStream()) {
            System.out.println("Downloading " + path);
            Files.copy(stream, Paths.get("downloads", repo, zip));
            System.out.println("Downloaded " + path);
        }
    }

    public void scanForIssues() {

    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setCreatedAt(String createdAt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'");
        try {
            this.createdAt = formatter.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setPublishedAt(String publishedAt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'");
        try {
            this.publishedAt = formatter.parse(publishedAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }
}
