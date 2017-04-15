package main;

import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Palash on 4/10/2017.
 */
public class Utils {
    private static long timestamp = new Date().getTime();

    static String getDownloadsFolderName() {
        return "downloads_" + timestamp;
    }

    static String getDownloadsFolderPath() {
        return System.getProperty("user.dir") + "/" + getDownloadsFolderName() + "/";
    }

    public static boolean deleteFromDownloadFolder(String path) {
        File f = new File(getDownloadsFolderPath(), path);
        return !f.exists() || f.delete();
    }

    public static boolean createInDownloadFolder(String path) {
        File r = new File(getDownloadsFolderPath(), path);
        return r.exists() || r.mkdir();
    }

    public static boolean createDownloads() {
        File f = new File(getDownloadsFolderPath());
        return f.exists() || f.mkdir();
    }

    public static boolean deleteDownloads() {
        File f = new File(getDownloadsFolderPath());
        return !f.exists() || f.delete();
    }
}
