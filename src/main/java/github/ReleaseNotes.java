package github;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Palash on 4/10/2017.
 */
public class ReleaseNotes {
    private String downloadURL;
    private String tagName;
    private Date createdAt;
    private Date publishedAt;
    private String name;

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getDownloadURL() {
        return downloadURL;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
