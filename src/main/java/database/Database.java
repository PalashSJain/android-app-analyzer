package database;

import android.Manifest;
import android.Permission;
import github.Issue;
import github.ReleaseNote;
import main.Library;
import main.Release;
import main.Repository;
import testinfo.MethodInfo;
import testinfo.TestInfo;

import java.sql.*;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Palash on 4/17/2017.
 */
public class Database {
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_USER = "palash";
    private static final String DB_URL = "jdbc:sqlite:P:\\dev\\android-app-analyzer\\data\\AAA.db";
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

    public void createTables() throws SQLException, ClassNotFoundException {
        String qMethodInfo = "Create table IF NOT EXISTS MethodInfo(" +
                "MethodInfoID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "Name VARCHAR(50) NOT NULL, " +
                "LinesOfCode INT(3), " +
                "TestInfoID INT," +
                "FOREIGN KEY (TestInfoID) REFERENCES TestInfo (TestInfoID))";

        String qTestInfo = "Create table IF NOT EXISTS TestInfo(" +
                "TestInfoID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "Name VARCHAR(50) NOT NULL, " +
                "ReleaseID INT," +
                "FOREIGN KEY (ReleaseID) REFERENCES Release (ReleaseID))";

        String qPermission = "Create table if not exists Permission(" +
                "PermissionID int AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "PName VARCHAR(50) NOT NULL UNIQUE, " +
                "ManifestID INT," +
                "FOREIGN KEY (ManifestID) REFERENCES Manifest (ManifestID))";

        String qManifest = "Create table IF NOT EXISTS Manifest(" +
                "ManifestID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "MinSDK INT, " +
                "MaxSDK INT, " +
                "TargetSDK INT, " +
                "ReleaseID INT," +
                "FOREIGN KEY (ReleaseID) REFERENCES Release (ReleaseID))";

        String qReleaseNote = "Create table IF NOT EXISTS ReleaseNote(" +
                "ReleaseNoteID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "DownloadURL varchar(50), " +
                "CreatedAt Date, " +
                "PublishedAt Date, " +
                "TagName varchar(50), " +
                "Name varchar(50), " +
                "ReleaseID INT," +
                "FOREIGN KEY (ReleaseID) REFERENCES Release (ReleaseID))";

        String qIssue = "Create table IF NOT EXISTS Issue(" +
                "IssueID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "State varchar(50), " +
                "CreatedAt Date, " +
                "UpdatedAt Date, " +
                "ClosedAt Date," +
                "ReleaseID INT," +
                "FOREIGN KEY (ReleaseID) REFERENCES Release (ReleaseID))";

        String qRelease = "Create table IF NOT EXISTS Release(" +
                "ReleaseID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "Repo VARCHAR(50) NOT NULL, " +
                "TestInfoID INT, " +
                "ManifestID INT, " +
                "ReleaseNoteID INT, " +
                "IssueID INT)";

        String qRepository = "Create table IF NOT EXISTS Repository(" +
                "repository_id integer PRIMARY KEY," +
                "repository_name text NOT NULL UNIQUE)";

        Statement stmt = null;
        try {
            open();
            stmt = connection.createStatement();
            stmt.executeUpdate(qPermission);

            stmt = connection.createStatement();
            stmt.executeUpdate(qManifest);

            stmt = connection.createStatement();
            stmt.executeUpdate(qMethodInfo);

            stmt = connection.createStatement();
            stmt.executeUpdate(qTestInfo);

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
//        Class.forName(DB_DRIVER);
//        connection = DriverManager.getConnection(DB_URL, DB_USER,
//                DB_PASSWORD);
        connection = DriverManager.getConnection(DB_URL);
    }

    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public int addRepository(Repository repository) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into Repository (repository_name) values(?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, repository.getRepoName());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()){
                resultId=rs.getInt(1);
            }
            rs.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
            return resultId;
        }
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
        try {
            open();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }

    public void addReleaseNote(Release release, ReleaseNote releaseNotes) {

    }

    public void addIssues(Release release, Stack<Issue> issues) {

    }

    public void addPermission(Manifest manifest, Permission p) throws SQLException, ClassNotFoundException {
        open();

        close();
    }
}
