package main;

import android.Manifest;
import github.Issue;
import github.ReleaseNotes;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import tools.DependencyCheck;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Palash on 4/9/2017.
 */
public class Release {
    private Set<Library> libraries;
    private List<Manifest> manifests;
    private ReleaseNotes notes;

    private String repo;
    private String zip;
    private final static String ANDROID_MANIFEST_XML = "AndroidManifest.xml";
    private Stack<Issue> issues;

    public Release() {
        this.notes = new ReleaseNotes();
        this.manifests = new ArrayList<>();
        this.libraries = new HashSet<>();
    }

    public void delete() {
        System.out.println("Deleting " + getPath());
        if (Utils.deleteFromDownloadFolder(getPath())) {
            System.out.println("Deleted " + getPath());
        } else {
            System.out.println("Failed to delete " + getPath());
        }
    }

    private String getFullPath() {
        return Utils.getDownloadsFolderPath() + getPath();
    }

    public void scanManifestFiles() {
        try {
            ZipFile zipFile = new ZipFile(getFullPath());
            Enumeration e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                if (entry.getName().endsWith(ANDROID_MANIFEST_XML)) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(zipFile.getInputStream(entry));
                    doc.getDocumentElement().normalize();
                    Manifest manifest = new Manifest();
                    manifest.scan(doc);
                    manifests.add(manifest);
                }
            }
            zipFile.close();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public synchronized void download() throws IOException {
        zip = notes.getTagName() + ".zip";
        URL url = new URL(notes.getDownloadURL());
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        try (InputStream stream = con.getInputStream()) {
            System.out.println("Downloading " + getPath());
            Files.copy(stream, Paths.get(Utils.getDownloadsFolderName(), repo, zip));
            System.out.println("Downloaded " + getPath());
        }
    }

    public void scanLibraries() {
        DependencyCheck dc = new DependencyCheck();
        dc.setProjectName(notes.getTagName());
        dc.setReportsFolder(Config.getReportsFolder() + "/" + repo + "/" + notes.getTagName());
        dc.initialize(getFullPath());
        dc.scan();
        libraries = dc.getLibraries();
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

    public String getPath() {
        return repo + "/" + zip;
    }

    public List<Manifest> getManifests() {
        return manifests;
    }

    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setIssues(Stack<Issue> issues) {
        this.issues = issues;
    }

    public List<Issue> getIssues() {
        return issues;
    }
}
