package tools;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Palash on 4/15/2017.
 */
public class DependencyCheckTest {
    DependencyCheck dc;

    @Before
    public void setUp() throws Exception {
        dc = new DependencyCheck();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void scan() throws Exception {
        dc.initialize("P:\\RAship\\rechecked");
        dc.scan();
    }

    @Test
    public void getLibraries(){
        dc.setReportsFolder("P:/dev/Archie/CodeScanner/Code/CLI/reports");
        dc.getLibraries();
    }

}