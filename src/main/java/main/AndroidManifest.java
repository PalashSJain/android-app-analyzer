package main;

import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class AndroidManifest {
    private int minSDK;
    private List<AndroidPermission> permissions;
    private int maxSDK;

    public int getMinSDK() {
        return minSDK;
    }

    public List<AndroidPermission> getPermissions() {
        return permissions;
    }

    public int getMaxSDK() {
        return maxSDK;
    }

    public void setMaxSDK(int maxSDK) {
        this.maxSDK = maxSDK;
    }
}
