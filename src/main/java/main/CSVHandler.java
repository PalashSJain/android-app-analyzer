package main;

import java.io.*;
import java.util.ArrayList;
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

    public void write(String file, String[] data) {

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
