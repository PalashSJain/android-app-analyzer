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
        File f = new File(getDownloadsFolderPath(), path);
        return f.exists() || f.mkdir();
    }

    public static boolean createDownloads() {
        File f = new File(getDownloadsFolderPath());
        return f.exists() || f.mkdir();
    }

    public static boolean deleteDownloads() {
        File f = new File(getDownloadsFolderPath());
        return !f.exists() || f.delete();
    }

    public static int getLastFileSeparator(String file) {
        if (file.contains("*") || file.contains("?")) {
            int p1 = file.indexOf('*');
            int p2 = file.indexOf('?');
            p1 = p1 > 0 ? p1 : file.length();
            p2 = p2 > 0 ? p2 : file.length();
            int pos = p1 < p2 ? p1 : p2;
            pos = file.lastIndexOf('/', pos);
            return pos;
        } else {
            return file.lastIndexOf('/');
        }
    }

    public static boolean isTrue(String value) {
        if (value == null) return false;
        return value.equalsIgnoreCase("true") ;
    }

    public static Properties fillWithPropertiesFile(String file) {
        Properties properties = new Properties();
        String appDir = System.getProperty("user.dir");
        InputStream input = null;
        try {
            input = new FileInputStream(appDir + "/userdata/" + file);
            properties.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return properties;
    }
}
