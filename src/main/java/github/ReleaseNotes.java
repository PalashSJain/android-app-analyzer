package github;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Palash on 4/10/2017.
 */
public class ReleaseNotes {
    private String downloadURL;
    private String tagName;
    private Calendar createdAt;
    private Calendar publishedAt;
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
        this.createdAt = Calendar.getInstance();
        try {
            this.createdAt.setTimeInMillis(formatter.parse(createdAt).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setPublishedAt(String publishedAt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'");
        this.publishedAt = Calendar.getInstance();
        try {
            this.publishedAt.setTimeInMillis(formatter.parse(publishedAt).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Calendar getPublishedAt() {
        return publishedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
