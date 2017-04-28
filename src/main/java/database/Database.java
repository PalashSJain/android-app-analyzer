package database;

import android.BuildGradle;
import android.Manifest;
import android.Permission;
import github.Issue;
import github.ReleaseNote;
import main.Library;
import main.Release;
import org.sqlite.SQLiteException;
import testinfo.MethodInfo;
import testinfo.TestInfo;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

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
        String qBuildGradle = "Create table if not exists BuildGradle(" +
                "build_gradle_id INTEGER PRIMARY KEY, " +
                "MinSDK INT, " +
                "MaxSDK INT, " +
                "TargetSDK INT, " +
                "release_id INT," +
                "FOREIGN KEY (release_id) REFERENCES Release (release_id))";

        String qMethodInfo = "Create table IF NOT EXISTS MethodInfo(" +
                "method_info_id INTEGER PRIMARY KEY, " +
                "Name VARCHAR(50) NOT NULL, " +
                "LinesOfCode INT(3), " +
                "test_info_id INT," +
                "FOREIGN KEY (test_info_id) REFERENCES TestInfo (test_info_id))";

        String qTestInfo = "Create table IF NOT EXISTS TestInfo(" +
                "test_info_id INTEGER PRIMARY KEY, " +
                "Name VARCHAR(50) NOT NULL, " +
                "release_id INT," +
                "FOREIGN KEY (release_id) REFERENCES Release (release_id))";

        String qPermission = "Create table IF not exists Permission(" +
                "permission_id integer PRIMARY KEY, " +
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
                "ReleaseNoteID integer PRIMARY KEY, " +
                "DownloadURL varchar(50), " +
                "CreatedAt Date, " +
                "PublishedAt Date, " +
                "TagName varchar(50), " +
                "Name varchar(50), " +
                "release_id INT," +
                "FOREIGN KEY (release_id) REFERENCES Release (release_ida" +
                "))";

        String qIssue = "Create table IF NOT EXISTS Issue(" +
                "issue_id INTEGER PRIMARY KEY, " +
                "State varchar(50), " +
                "CreatedAt Date, " +
                "UpdatedAt Date, " +
                "ClosedAt Date," +
                "release_id INT," +
                "FOREIGN KEY (release_id) REFERENCES Release (release_id))";

        String qLibrary = "Create table IF NOT EXISTS Library(" +
                "library_id INTEGER PRIMARY KEY, " +
                "Vulnerable Boolean, " +
                "Name Varchar(50), " +
                "release_id INT," +
                "FOREIGN KEY (release_id) REFERENCES Release (release_id))";

        String qRelease = "Create table IF NOT EXISTS Release(" +
                "release_id integer PRIMARY KEY, " +
                "repository_id int, " +
                "FOREIGN KEY (repository_id) REFERENCES Repository (repository_id))";

        String qRepository = "Create table IF NOT EXISTS Repository(" +
                "repository_id integer PRIMARY KEY," +
                "repository_name VARCHAR (50))";

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
            stmt.executeUpdate(qLibrary);

            stmt = connection.createStatement();
            stmt.executeUpdate(qRepository);

            stmt = connection.createStatement();
            stmt.executeUpdate(qAndroidPermissions);

            stmt = connection.createStatement();
            stmt.executeUpdate(qBuildGradle);

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
            } catch (IOException | SQLiteException e) {
                e.printStackTrace();
            }
        } finally {
            close();
        }
    }

    public void purge() {
        try {
            open();
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS ReleaseNote");
            statement.execute("DROP TABLE IF EXISTS Permission");
            statement.execute("DROP TABLE IF EXISTS MethodInfo");
            statement.execute("DROP TABLE IF EXISTS Issue");
            statement.execute("DROP TABLE IF EXISTS BuildGradle");
            statement.execute("DROP TABLE IF EXISTS TestInfo");
            statement.execute("DROP TABLE IF EXISTS Release");
            statement.execute("DROP TABLE IF EXISTS Manifest");
            statement.execute("DROP TABLE IF EXISTS Repository");
            statement.execute("DROP TABLE IF EXISTS AndroidPermissions");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        close();
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

    public int addLibrary(int release_id, Library library) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into Library (name, Vulnerable, release_id) values(?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, library.getName());
            statement.setInt(2, library.isVulnerable() ? 1 : 0);
            statement.setInt(3, release_id);
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

    public int addTestInfos(int release_id, TestInfo testInfo) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into TestInfo (name, release_id) values(?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, testInfo.getName());
            statement.setInt(2, release_id);
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

    public int addMethodInfos(int test_info_id, MethodInfo method) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into MethodInfo (name, linesOfCode, test_info_id) values(?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, method.getName());
            statement.setInt(2, method.getLinesOfCode());
            statement.setInt(3, test_info_id);
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

    public int addReleaseNote(int release_id, ReleaseNote note) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into ReleaseNote (DownloadURL, CreatedAt, PublishedAt, TagName, Name, release_id) values(?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, note.getDownloadURL());
            statement.setString(2, String.valueOf(note.getCreatedAt()));
            statement.setString(3, String.valueOf(note.getPublishedAt()));
            statement.setString(4, note.getTagName());
            statement.setString(5, note.getName());
            statement.setInt(6, release_id);
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

    public int addIssues(int release_id, Issue issue) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into Issue (State, CreatedAt, UpdatedAt, ClosedAt, release_id) values(?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, issue.getState());
            statement.setString(2, String.valueOf(issue.getCreatedAt()));
            statement.setString(3, String.valueOf(issue.getUpdatedAt()));
            java.sql.Date d = issue.getClosedAt();
            if (d != null) statement.setString(4, String.valueOf(d));
            else statement.setString(4, null);
            statement.setInt(5, release_id);
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

    public int addBuildGradle(int id, BuildGradle buildGradle) {
        int resultId = -1;
        try {
            open();
            String query = "Insert into BuildGradle (release_id, MinSDK, MaxSDK, TargetSDK) values(?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            statement.setInt(2, buildGradle.getMinSdkVersion());
            statement.setInt(3, buildGradle.getMaxSdkVersion());
            statement.setInt(4, buildGradle.getTargetSdkVersion());
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
