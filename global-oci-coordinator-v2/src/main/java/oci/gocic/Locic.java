package oci.gocic;

import java.net.InetAddress;

public class Locic {
    
    public Locic(InetAddress ip, String subnet, String location) {
        this.ip         = ip;
        this.subnet     = subnet;
        this.location   = location;
    }
    
    private InetAddress ip          = null;
    private String      subnet      = null;
    private String      location    = null;

} // class
