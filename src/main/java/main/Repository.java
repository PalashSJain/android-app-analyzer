package main;

import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Repository implements Comparable<Repository>{
    private List<Release> releases;

    public Repository(List<Release> releases) {

    }

    public int compareTo(Repository repo) {
        return 0;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }
}
