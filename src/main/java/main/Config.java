package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Palash on 4/15/2017.
 */
public class Config {

    private static Properties properties;
    private static Properties token;

    static void initialize() {
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

    public static boolean isDebugModeOn() {
        return Boolean.parseBoolean(properties.getProperty("debug.mode", "false"));
    }

    public static String getToken() {
        return token.getProperty("token");
    }

    public static String getTokenAgent() {
        return token.getProperty("agent");
    }

    public static int getMinNumberOfReleases() {
        return Integer.parseInt(properties.getProperty("limitation.min_number_of_releases", "0"));
    }

    public static int getMaxRepositories() {
        return Integer.parseInt(properties.getProperty("limitation.max_number_of_repositories", "0"));
    }

    public static int getDifferenceBetweenReleases(){
        return Integer.parseInt(properties.getProperty("limitation.difference_between_releases", "0"));
    }

    public static String getReportsFolder() {
        return System.getProperty("user.dir") + "/reports/";
    }
}
