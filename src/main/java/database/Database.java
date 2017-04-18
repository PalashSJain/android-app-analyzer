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

    public void createTables() throws SQLException, ClassNotFoundException {
        String qMethodInfo = "Create table IF NOT EXISTS MethodInfo(" +
                "MethodInfoID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "Name VARCHAR(50) NOT NULL, " +
                "LinesOfCode INT(3))";

        String qTestInfo = "Create table IF NOT EXISTS TestInfo(" +
                "TestInfoID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "Name VARCHAR(50) NOT NULL, " +
                "MethodInfoID INT, " +
                "FOREIGN KEY (MethodInfoID) REFERENCES MethodInfo (MethodInfoID))";

        String qPermission = "Create table if not exists Permission(" +
                "PermissionID int AUTO_INCREMENT PRIMARY KEY NOT NULL," +
                "Name VARCHAR(50) NOT NULL)";

        String qManifest = "Create table IF NOT EXISTS Manifest(" +
                "ManifestID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "MinSDK INT, " +
                "MaxSDK INT, " +
                "TargetSDK INT, " +
                "PermissionID INT, " +
                "FOREIGN KEY (PermissionID) REFERENCES Permission (PermissionID))";

        String qReleaseNote = "Create table IF NOT EXISTS ReleaseNote(" +
                "ReleaseNoteID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "DownloadURL varchar(50), " +
                "CreatedAt Date, " +
                "PublishedAt Date, " +
                "TagName varchar(50), " +
                "Name varchar(50))";

        String qIssue = "Create table IF NOT EXISTS Issue(" +
                "IssueID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "State varchar(50), " +
                "CreatedAt Date, " +
                "UpdatedAt Date, " +
                "ClosedAt Date)";

        String qRelease = "Create table IF NOT EXISTS Release(" +
                "ReleaseID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "Repo VARCHAR(50) NOT NULL, " +
                "TestInfoID INT, " +
                "FOREIGN KEY (TestInfoID) REFERENCES TestInfo (TestInfoID)," +
                "ManifestID INT, " +
                "FOREIGN KEY (ManifestID) REFERENCES Manifest (ManifestID)," +
                "ReleaseNoteID INT, " +
                "FOREIGN KEY (ReleaseNoteID) REFERENCES ReleaseNote (ReleaseNoteID)," +
                "IssueID INT, " +
                "FOREIGN KEY (IssueID) REFERENCES Issue (IssueID))";

        String qRepository = "Create table IF NOT EXISTS Repository(" +
                "ReleaseID INT, " +
                "FOREIGN KEY (ReleaseID) REFERENCES Release (ReleaseID))";

        Statement stmt = null;
        try {
            open();
            stmt = connection.createStatement();
            stmt.executeUpdate(qMethodInfo);

            stmt = connection.createStatement();
            stmt.executeUpdate(qTestInfo);

            stmt = connection.createStatement();
            stmt.executeUpdate(qPermission);

            stmt = connection.createStatement();
            stmt.executeUpdate(qManifest);

            stmt = connection.createStatement();
            stmt.executeUpdate(qReleaseNote);

            stmt = connection.createStatement();
            stmt.executeUpdate(qIssue);

            stmt = connection.createStatement();
            stmt.executeUpdate(qRelease);

            stmt = connection.createStatement();
            stmt.executeUpdate(qRepository);
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
