package github;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Palash on 4/16/2017.
 */
public class Issue {
    private String state;
    private Calendar createdAt, updatedAt, closedAt;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Calendar getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Calendar closedAt) {
        this.closedAt = closedAt;
    }

}
