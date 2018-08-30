package oci.gocic;

import java.net.InetAddress;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

/**
 * The LOCIC object class contains the information of a LOCIC
 * @author Marc Koerner
 */
public class Locic {
    
    private InetAddress     ip          = null;
    private SubnetUtils     subnet      = null;
    private String          location    = null;
    
    /**
     * Constructor with CIDR subnet notation, e.g. 192.168.1.1/24
     * @param ip
     * @param cidrSubnet
     * @param location
     */
    public Locic(InetAddress ip, String cidrSubnet, String location) {
        this.ip         = ip;
        this.subnet     = new SubnetUtils(cidrSubnet);
        this.location   = location;
    }
    
    /**
     * Constructor with netmask notation, e.g. 192.168.1.1 255.255.255.o
     * @param ip
     * @param subnetIp
     * @param subnetMask
     * @param location
     */
    public Locic(InetAddress ip, String subnetIp, String subnetMask, String location) {
        this.ip         = ip;
        this.subnet     = new SubnetUtils(subnetIp, subnetMask);
        this.location   = location;
    }
    
    public boolean belongsToSubnet(InetAddress ip) {   
        String      ip_string   = ip.getHostAddress();
        SubnetInfo  subnet      = this.subnet.getInfo();    
        return subnet.isInRange(ip_string);        
    }
    
    public InetAddress getIpAddr() {
        return this.ip;
    }
    
    public String getIpString() {
        return this.ip.getHostAddress();
    }

} // class
