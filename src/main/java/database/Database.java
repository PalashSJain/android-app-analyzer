package database;

import android.Manifest;
import android.Permission;
import github.Issue;
import github.ReleaseNote;
import main.Library;
import main.Release;
import testinfo.MethodInfo;
import testinfo.TestInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Scanner;
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

        String qPermission = "Create table IF not exists Permission(" +
                "permission_id integer PRIMARY KEY NOT NULL, " +
                "PName VARCHAR(50) NOT NULL, " +
                "manifest_id INT," +
                "FOREIGN KEY (manifest_id) REFERENCES Manifest (manifest_id))";

        String qManifest = "Create table IF NOT EXISTS Manifest(" +
                "manifest_id integer PRIMARY KEY NOT NULL, " +
                "MinSDK INT, " +
                "MaxSDK INT, " +
                "TargetSDK INT, " +
                "release_id INT," +
                "FOREIGN KEY (release_id) REFERENCES Release (release_id))";

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
                "release_id integer PRIMARY KEY, " +
                "repository_id int, " +
                "FOREIGN KEY (repository_id) REFERENCES Repository (repository_id))";

        String qRepository = "Create table IF NOT EXISTS Repository(" +
                "repository_id integer PRIMARY KEY," +
                "repository_name text NOT NULL UNIQUE)";

        String qAndroidPermissions = "Create table if not exists AndroidPermissions(" +
                "android_permission_id integer primary key, " +
                "android_permission_name text not null unique)";

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

            stmt = connection.createStatement();
            stmt.executeUpdate(qAndroidPermissions);

            try (Scanner scanner = new Scanner(new File(getClass().getClassLoader().getResource("permissions.csv").getFile()))) {
                StringBuilder q = new StringBuilder("");
                q.append("Insert into AndroidPermissions (android_permission_name) values ");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    q.append("('");
                    q.append(line);
                    q.append("'),");
                }
                q.deleteCharAt(q.lastIndexOf(","));
                scanner.close();
                stmt = connection.createStatement();
                stmt.execute(q.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public int addRepository(String repositoryName) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into Repository (repository_name) values(?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, repositoryName);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                resultId = rs.getInt(1);
            }
            rs.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return resultId;
    }

    public int addRelease(int repositoryId) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into Release (repository_id) values(?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, repositoryId);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                resultId = rs.getInt(1);
            }
            rs.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return resultId;
    }

    public void addLibraries(Release release, Set<Library> libraries) {

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

    public int addPermission(int manifestId, Permission p) throws SQLException, ClassNotFoundException {
        int resultId = -1;
        try {
            open();
            String query = "Insert into Permission (manifest_id, PName) values(?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, manifestId);
            statement.setString(2, p.getName());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                resultId = rs.getInt(1);
            }
            rs.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return resultId;
    }

    public int addManifest(int releaseId, Manifest manifest) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into Manifest (release_id, MinSDK, MaxSDK, TargetSDK) values(?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, releaseId);
            statement.setInt(2, manifest.getMinSDK());
            statement.setInt(3, manifest.getMaxSDK());
            statement.setInt(4, manifest.getTargetSDK());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                resultId = rs.getInt(1);
            }
            rs.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return resultId;
    }
}
