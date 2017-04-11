package main;

import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Palash on 4/10/2017.
 */
public class Utils {
    private static Properties properties;
    private static Properties token;
    private static long timestamp = 0;

    static void initialize(){
        timestamp = new Date().getTime();
        properties = new Properties();
        token = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("AndroidAppAnalyzer.properties");
            properties.load(input);

            input = new FileInputStream("OAuthToken.properties");
            token.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String getDownloadsFolderName(){
        return "downloads_"+timestamp;
    }

    static String getDownloadsFolderPath(){
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

    public static boolean isDebugModeOn(){
        return Boolean.parseBoolean(properties.getProperty("debug.mode", "false"));
    }

    public static String getToken(){
        return token.getProperty("token");
    }

    public static String getTokenAgent(){
        return token.getProperty("agent");
    }

    public static boolean createDownloads() {
        File f = new File(getDownloadsFolderPath());
        return f.exists() || f.mkdir();
    }

    public static boolean deleteDownloads(){
        File f = new File(getDownloadsFolderPath());
        return !f.exists() || f.delete();
    }
}
