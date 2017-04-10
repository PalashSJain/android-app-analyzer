package main;

import com.sun.javaws.jnl.LibraryDesc;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Release {
    private int minSDK;
    private int maxSDK;
    private List<AndroidPermission> permissions;
    private List<Library> safeLibraries;
    private List<Library> vulnLibraries;
    private String path;
    private String downloadURL;
    private String name;
    private String tagName;
    private Date createdAt;
    private Date publishedAt;

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

    }

    public void parseManifest() {

    }

    public void download() {

    }

    public String getPath() {
        return path;
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
}
