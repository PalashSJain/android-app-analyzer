package database;

import android.Manifest;
import github.Issue;
import github.ReleaseNote;
import main.Library;
import main.Release;
import main.Repository;
import testinfo.MethodInfo;
import testinfo.TestInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Palash on 4/17/2017.
 */
public class Database {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_USER = "palash";
    private static final String DB_URL = "jdbc:h2:data/aaa";
    private static final String DB_PASSWORD = "a";
    private static Database instance = null;
    private static Connection connection;

    private Database() {

    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void createDatabase() {
//        Statement statement = null;
//        try {
//            open();
//            statement = connection.createStatement();
//            String query = "CREATE DATABASE AAA";
//            statement.executeUpdate(query);
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (statement != null)
//                    statement.close();
//            } catch (SQLException se2) {
//            }// nothing we can do
//            close();
//        }
    }

    public void createTables() {
        String qMethodInfo = "Create table IF NOT EXISTS MethodInfo(" +
                "MethodInfoID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "Name VARCHAR(50) NOT NULL, " +
                "LinesOfCode INT(3))";

        String qTestInfo = "Create table IF NOT EXISTS TestInfo(" +
                "TestInfoID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "Name VARCHAR(50) NOT NULL, " +
                "MethodInfoID INT, " +
                "FOREIGN KEY (MethodInfoID) REFERENCES MethodInfo (MethodInfoID))";

        Statement stmt = null;
        try {
            open();
            stmt = connection.createStatement();
            stmt.executeUpdate(qMethodInfo);

            stmt = connection.createStatement();
            stmt.executeUpdate(qTestInfo);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void purge() {

    }

    public void open() throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);
        connection = DriverManager.getConnection(DB_URL, DB_USER,
                DB_PASSWORD);
    }

    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void addRepository(Repository repository) {

    }

    public void addRelease(Repository repository, Release release) {

    }

    public void addLibraries(Release release, Set<Library> libraries) {

    }

    public void addManifests(Release release, List<Manifest> manifests) {

    }

    public void addTestInfos(Release release, List<TestInfo> testInfos) {

    }

    public void addMethodInfos(TestInfo testInfo, List<MethodInfo> methods) {

    }

    public void addReleaseNote(Release release, ReleaseNote releaseNotes) {

    }

    public void addIssues(Release release, Stack<Issue> issues) {

    }
}
