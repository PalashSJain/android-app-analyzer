package database;

import android.Manifest;
import github.Issue;
import github.ReleaseNote;
import main.Library;
import main.Release;
import main.Repository;
import testinfo.MethodInfo;
import testinfo.TestInfo;

import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Palash on 4/17/2017.
 */
public class Database {
    private static Database instance = null;

    private Database() {

    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void createDatabase(){

    }

    public void createTables() {

    }

    public void purge(){

    }

    public void open() {

    }

    public void close() {

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
