package main;

/**
 * Created by Palash on 4/9/2017.
 */
public class Library {
    private boolean isVulnerable;
    private String name;
    private int id;

    public boolean isVulnerable() {
        return isVulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        isVulnerable = vulnerable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Library obj) {
        return this.getName().equals(obj.getName());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
