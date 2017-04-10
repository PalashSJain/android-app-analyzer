package main;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Repository implements Comparable<Repository> {
    private List<Release> releases;
    private Link link;

    public Repository(List<Release> releases) {
        this.releases = releases;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

    @Override
    public int compareTo(Repository o) {
        return o.getReleases().size() - this.getReleases().size();
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
