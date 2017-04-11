package main;

import github.ReleaseNotes;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Release {
    private String path;
    private String zip;
    private List<Library> safeLibraries;
    private List<Library> vulnLibraries;
    private String name;
    private ReleaseNotes notes;
    private String repo;

    public Release(){
        this.notes = new ReleaseNotes();
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
        // Get Manifest file
        // Create Manifest object
        // parse manifest: get minSDK, maxSDK, Permissions
    }

    public synchronized void download() throws IOException {
        zip = notes.getTagName() + ".zip";
        path = repo + "/" + zip;
        URL url = new URL(notes.getDownloadURL());
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        try (InputStream stream = con.getInputStream()) {
            System.out.println("Downloading " + path);
            Files.copy(stream, Paths.get("downloads", repo, zip));
            System.out.println("Downloaded " + path);
        }
    }

    public void scanForLibraries() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setReleaseNotes(ReleaseNotes releaseNotes) {
        this.notes = releaseNotes;
    }

    public ReleaseNotes getReleaseNotes() {
        return notes;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public void analyze() {
        try {
            download();
        } catch (IOException e) {
            System.out.println("Failed to download " + getName() + ". Going to next release.");
            return;
        }
        parseManifest();
        scanForLibraries();
        if (!Utils.isDebugModeOn()) delete();
        System.out.println("");
    }
}
