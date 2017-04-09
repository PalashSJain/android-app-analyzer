package main;

import com.sun.javaws.jnl.LibraryDesc;

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
}
