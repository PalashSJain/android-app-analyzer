package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Palash on 4/9/2017.
 */
public class CSVHandler {
    private final String csv;
    private List<Link> links;
    final String __DELIMITER__ = ",";

    public CSVHandler(String csv) {
        this.csv = csv;
    }

    public void write(List<Repository> repositories) {
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new FileWriter("new_output.csv"));
            StringBuilder sb = new StringBuilder();
            for (Repository repository : repositories) {
//                for (Release release : repository.getReleases()) {
//                    sb.append(release.getName());
//                    sb.append(__DELIMITER__);
                    sb.append(repository.getLink());
                    sb.append(__DELIMITER__);
//                    sb.append(release.getTagName());
//                    sb.append(__DELIMITER__);
                    sb.append(repository.getReleases().size());
//                    sb.append(__DELIMITER__);
                    sb.append("\n");
//                }
            }
            br.write(sb.toString());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized List<Link> getLinks() {
        return links;
    }

    public void parse() throws IOException {
        links = new ArrayList<>();
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(CSVHandler.class.getResource(csv).getFile()));
            while ((line = br.readLine()) != null) {
                String[] info = line.split(__DELIMITER__);
                links.add(new Link(info[0]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
