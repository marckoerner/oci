package oci.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import oci.gocic.GocicConfig;
import oci.gocic.Locic;

public class GocicConfigTest {
    
    private GocicConfig     config = null;


    @Before
    public void setUp() throws Exception {
        this.config = new GocicConfig(new File("./GocicConfig.json"));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseConfigTest() {
        assertTrue(this.config.parseConfig());
        
        Vector<Locic>  locics = this.config.getLocics();
        assertNotNull(locics);
        
        // TODO: check locic mock values
        
    } // parseConfigTest

} // class
