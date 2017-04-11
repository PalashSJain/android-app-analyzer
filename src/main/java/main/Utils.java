package main;

import java.io.*;
import java.util.Properties;

/**
 * Created by Palash on 4/10/2017.
 */
public class Utils {
    private static Properties properties;
    private static Properties token;

    static void initialize(){
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

    static String getDownloadFolder(){
        return System.getProperty("user.dir") + "/downloads/";
    }

    public static boolean deleteFromDownloadFolder(String path) {
        File f = new File(getDownloadFolder(), path);
        return !f.exists() || f.delete();
    }

    public static boolean createInDownloadFolder(String path) {
        File r = new File(getDownloadFolder(), path);
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
        File f = new File(getDownloadFolder());
        return f.exists() || f.mkdir();
    }
}
