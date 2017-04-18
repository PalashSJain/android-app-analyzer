package android;

/**
 * Created by Palash on 4/18/2017.
 */
public class Permission {
    private final String name;
    private int id;

    public Permission(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
