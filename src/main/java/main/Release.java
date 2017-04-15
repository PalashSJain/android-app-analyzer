package main;

import android.Manifest;
import github.ReleaseNotes;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
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
    private List<Library> libraries;
    private ReleaseNotes notes;
    private Manifest manifest;
    private String repo;

    public Release(){
        this.notes = new ReleaseNotes();
    }

    public void delete() {
        System.out.println("Deleting " + getPath());
        if (Utils.deleteFromDownloadFolder(getPath())) {
            System.out.println("Deleted " + getPath());
        } else {
            System.out.println("Failed to delete " + getPath());
        }
    }

    private String getFullPath(){
        return Utils.getDownloadsFolderPath() +"/"+ getPath();
    }

    public void scanManifest() {
        manifest = new Manifest();
        if (manifest.find(new File(getFullPath()))){
            manifest.scan();
        } else {
            System.out.println("Failed to find Manifest in " + getFullPath());
        }
//        manifest.set();
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
            System.out.println("Downloading " + getPath());
            Files.copy(stream, Paths.get(Utils.getDownloadsFolderName(), repo, zip));
            System.out.println("Downloaded " + getPath());
        }
    }

    public void scanForLibraries() {

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
            System.out.println("Failed to download " + getPath());
            return;
        }
        scanForLibraries();
        scanManifest();
        if (!Config.isDebugModeOn()) delete();
        System.out.println("");
    }

    public String getPath() {
        return path;
    }
}
