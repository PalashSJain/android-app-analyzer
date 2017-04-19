package android;

/**
 * Created by Palash on 4/18/2017.
 */
public class BuildGradle {
    private String minSdkVersion, maxSdkVersion, targetSdkVersion;

    public String getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(String targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public String getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(String minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public String getMaxSdkVersion() {
        return maxSdkVersion;
    }

    public void setMaxSdkVersion(String maxSdkVersion) {
        this.maxSdkVersion = maxSdkVersion;
    }
}
