package android;

import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class Manifest {
    private int minSDK;
    private List<Permission> permissions;
    private int maxSDK;

    public int getMinSDK() {
        return minSDK;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public int getMaxSDK() {
        return maxSDK;
    }

    public void setMaxSDK(int maxSDK) {
        this.maxSDK = maxSDK;
    }
}
