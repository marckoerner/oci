/**
 * 
 */
package oci.test;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;
import oci.gocic.Locic;

/**
 * @author Marc Koerner
 *
 */
public class LocicTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    // @Test
    // public void test() {
    //     fail("Not yet implemented");
    // }
    
    @Test
    public void belongsToSubnet() {
        
        // Initialize locic object
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Locic locic_1 = new Locic(ip, "127.0.0.1/24", "us.ca.berkeley");
        
        // ip in subnet
        Assert.assertTrue(locic_1.belongsToSubnet(ip));
        
        locic_1 = new Locic(ip, "192.168.0.1/24", "us.ca.berkeley");
        
        // ip not in sub-net
        Assert.assertFalse(locic_1.belongsToSubnet(ip));
        
    }

} // class
