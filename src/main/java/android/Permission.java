package android;

/**
 * Created by Palash on 4/18/2017.
 */
public class Permission {
    private final String name;

    public Permission(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
