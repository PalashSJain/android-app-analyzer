package android;

/**
 * Created by Palash on 4/18/2017.
 */
public class BuildGradle {
    private int minSdkVersion, maxSdkVersion, targetSdkVersion;
    private int id;

    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(String targetSdkVersion) {
        this.targetSdkVersion = Integer.parseInt(targetSdkVersion);
    }

    public int getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(String minSdkVersion) {
        this.minSdkVersion = Integer.parseInt(minSdkVersion);
    }

    public int getMaxSdkVersion() {
        return maxSdkVersion;
    }

    public void setMaxSdkVersion(String maxSdkVersion) {
        this.maxSdkVersion = Integer.parseInt(maxSdkVersion);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
