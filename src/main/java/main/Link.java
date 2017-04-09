package main;

import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Link {
    private List<Release> releases;
    private String link;

    public Link(String link) {
        this.link = link;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public String get() {
        return link;
    }

}
