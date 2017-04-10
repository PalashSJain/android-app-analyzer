package tools;

import main.Library;

import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class DependencyCheck {
    private List<Library> safeLibraries;
    private List<Library> vulnLibraries;

    public void scan(String folder) {
        
    }

    public List<Library> getSafeLibraries() {
        return safeLibraries;
    }

    public void setSafeLibraries(List<Library> safeLibraries) {
        this.safeLibraries = safeLibraries;
    }

    public List<Library> getVulnLibraries() {
        return vulnLibraries;
    }

    public void setVulnLibraries(List<Library> vulnLibraries) {
        this.vulnLibraries = vulnLibraries;
    }
}
