package tools;

import exceptions.InvalidScanPathException;
import main.Config;
import main.Library;
import main.Utils;
import org.apache.tools.ant.DirectoryScanner;
import org.owasp.dependencycheck.Engine;
import org.owasp.dependencycheck.data.nvdcve.CveDB;
import org.owasp.dependencycheck.data.nvdcve.DatabaseException;
import org.owasp.dependencycheck.data.update.exception.UpdateException;
import org.owasp.dependencycheck.dependency.Dependency;
import org.owasp.dependencycheck.exception.ExceptionCollection;
import org.owasp.dependencycheck.exception.ReportException;
import org.owasp.dependencycheck.reporting.ReportGenerator;
import org.owasp.dependencycheck.utils.InvalidSettingException;
import org.owasp.dependencycheck.utils.Settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Palash on 4/9/2017.
 */
public class DependencyCheck {
    List<Library> libraries;
    String folder;
    private String reportsFolder;
    private String projectName;

    public DependencyCheck() {
        libraries = new ArrayList<>();
    }

    public List<Library> scan() {
        Engine engine = null;
        try {
            final Set<File> paths = new HashSet<>();
            System.out.println("Scanning " + folder);
            String include = folder.replace('\\', '/');
            File baseDir;

            if (include.startsWith("//")) {
                throw new InvalidScanPathException("Unable to scan paths specified by //");
            } else {
                final int pos = Utils.getLastFileSeparator(include);
                final String tmpBase = include.substring(0, pos);
                final String tmpInclude = include.substring(pos + 1);
                if ((new File(include)).isFile()) {
                    baseDir = new File(tmpBase);
                    include = tmpInclude;
                } else {
                    baseDir = new File(tmpBase, tmpInclude);
                    include = "**/*";
                }
            }
            final DirectoryScanner scanner = getDirectoryScanner(0, new String[]{}, include, baseDir);
            scanner.scan();
            if (scanner.getIncludedFilesCount() > 0) {
                for (String s : scanner.getIncludedFiles()) {
                    final File f = new File(baseDir, s);
                    paths.add(f);
                }
            }
            engine = new Engine();
            try {
                engine.doUpdates();
            } catch (UpdateException e) {
                e.printStackTrace();
            }
            engine.scan(paths);

            generateReport(engine);
        } catch (DatabaseException | ReportException | InvalidScanPathException | ExceptionCollection e) {
            e.printStackTrace();
        } finally {
            if (engine != null) {
                engine.cleanup();
            }
        }
        return libraries;
    }

    private void generateReport(Engine engine) throws DatabaseException, ExceptionCollection, ReportException {
        ExceptionCollection exCol = null;
        final List<Dependency> dependencies = engine.getDependencies();
        CveDB cve = null;
        try {
            engine.analyzeDependencies();
            cve = new CveDB();
            cve.open();
            final ReportGenerator report = new ReportGenerator("AndroidAppAnalyzer", dependencies, engine.getAnalyzers(), cve.getDatabaseProperties());
            report.generateReports(reportsFolder, "XML");
        } catch (ExceptionCollection ex) {
            if (ex.isFatal()) {
                throw ex;
            }
            exCol = ex;
        } finally {
            if (cve != null) {
                cve.close();
            }
            if (exCol != null && exCol.getExceptions().size() > 0) {
                throw exCol;
            }
        }
    }

    private DirectoryScanner getDirectoryScanner(int symLinkDepth, String[] excludes, String include, File baseDir) {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(baseDir);
        final String[] includes = {include};
        scanner.setIncludes(includes);
        scanner.setMaxLevelsOfSymlinks(symLinkDepth);
        if (symLinkDepth <= 0) {
            scanner.setFollowSymlinks(false);
        }
        if (excludes != null && excludes.length > 0) {
            scanner.addExcludes(excludes);
        }
        return scanner;
    }

    public void initialize(String folder) {
        this.folder = folder;
        Properties properties = Utils.fillWithPropertiesFile("dependencycheck.properties");
        properties.setProperty("scan", folder);
        properties.setProperty("project", projectName);
        properties.setProperty(DependencyCheckArguments.OUT, System.getProperty("user.dir") + "/reports/");
        properties.setProperty(DependencyCheckArguments.OUTPUT_FORMAT, "XML");

        Settings.initialize();
        String propFile = properties.getProperty(DependencyCheckArguments.PROP, null);
        if (propFile != null && !propFile.isEmpty()) {
            final File propertiesFile = new File(propFile);
            try {
                Settings.mergeProperties(propertiesFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        final String dataDirectory = properties.getProperty(DependencyCheckArguments.DATA_DIRECTORY); //getDataDirectory();
        if (dataDirectory != null) {
            Settings.setString(Settings.KEYS.DATA_DIRECTORY, dataDirectory);
        } else if (System.getProperty("basedir") != null) {
            final File dataDir = new File(System.getProperty("basedir"), "data");
            Settings.setString(Settings.KEYS.DATA_DIRECTORY, dataDir.getAbsolutePath());
        }

        Settings.setBoolean(Settings.KEYS.AUTO_UPDATE, Utils.isTrue(properties.getProperty(DependencyCheckArguments.AUTO_UPDATE))); //canAutoUpdateDB());
        Settings.setStringIfNotEmpty(Settings.KEYS.SUPPRESSION_FILE, properties.getProperty(DependencyCheckArguments.SUPPRESSION_FILE)); //getSuppressionFile());
        Settings.setStringIfNotEmpty(Settings.KEYS.HINTS_FILE, properties.getProperty(DependencyCheckArguments.HINTS_FILE)); //getHintsFile());

        //File Type Analyzer Settings
        Settings.setBoolean(Settings.KEYS.ANALYZER_EXPERIMENTAL_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.EXPERIMENTAL_ENABLED))); //isExperimentalEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_JAR_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.JAR_ENABLED))); //isJarEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_ARCHIVE_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.ARCHIVE_ENABLED))); //isArchiveEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_PYTHON_DISTRIBUTION_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.PYTHON_DISTRIBUTION_ENABLED))); //isDistributedEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_PYTHON_PACKAGE_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.PYTHON_PACKAGE_ENABLED))); //isPythonPackageEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_AUTOCONF_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.AUTOCONF_ENABLED))); //isAutoconfEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_CMAKE_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.CMAKE_ENABLED))); //isCMakeEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_NUSPEC_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.NUSPEC_ENABLED))); //isNuspecEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_ASSEMBLY_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.ASSEMBLY_ENABLED))); //isAssemblyEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_OPENSSL_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.OPENSSL_ENABLED))); //isOpenSSLEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_COMPOSER_LOCK_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.COMPOSER_LOCK_ENABLED))); //isComposerLockEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_NODE_PACKAGE_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.NODE_PACKAGE_ENABLED))); //isNodePackageEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_SWIFT_PACKAGE_MANAGER_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.SWIFT_PACKAGE_MANAGER_ENABLED))); //isSwiftPackageManagerEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_COCOAPODS_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.COCOAPODS_ENABLED))); //isCocoapodsEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_RUBY_GEMSPEC_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.RUBY_GEMSPEC_ENABLED))); //isRubyGemspecEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_CENTRAL_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.CENTRAL_ENABLED))); //isCentralEnabled());
        Settings.setBoolean(Settings.KEYS.ANALYZER_NEXUS_ENABLED, Utils.isTrue(properties.getProperty(DependencyCheckArguments.NEXUS_ENABLED))); //isNexusEnabled());

        Settings.setStringIfNotEmpty(Settings.KEYS.ANALYZER_NEXUS_URL, properties.getProperty(DependencyCheckArguments.NEXUS_URL)); //getNexusURL());
        Settings.setBoolean(Settings.KEYS.ANALYZER_NEXUS_USES_PROXY, Utils.isTrue(properties.getProperty(DependencyCheckArguments.NEXUS_USES_PROXY))); //getNexusUsesProxy());
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_DRIVER_NAME, properties.getProperty(DependencyCheckArguments.DB_DRIVER)); //getDatabaseDriverName());
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_DRIVER_PATH, properties.getProperty(DependencyCheckArguments.DB_DRIVER_PATH)); //getDatabaseDriverPath());
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_CONNECTION_STRING, properties.getProperty(DependencyCheckArguments.CONNECTION_STRING)); //getConnectionString());
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_USER, properties.getProperty(DependencyCheckArguments.DB_NAME)); //getDatabaseUser());
        Settings.setStringIfNotEmpty(Settings.KEYS.DB_PASSWORD, properties.getProperty(DependencyCheckArguments.DB_PASSWORD)); //getDatabasePassword());
    }

    public void setReportsFolder(String reportsFolder) {
        this.reportsFolder = reportsFolder;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
