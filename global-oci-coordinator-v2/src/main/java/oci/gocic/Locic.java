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
     * Constructor with CIDR sub-net notation, e.g. 192.168.1.1/24
     * @param ip LOCIC's IP address
     * @param cidrSubnet sub-network in CIDR notation belonging the LOCIC's domain
     * @param location location covered by the regarding central office (e.g. US.CA.Berkeley)
     */
    public Locic(InetAddress ip, String cidrSubnet, String location) {
        this.ip         = ip;
        this.subnet     = new SubnetUtils(cidrSubnet);
        this.location   = location;
    }
    
    /**
     * Constructor with netmask notation, e.g. 192.168.1.1 255.255.255.o
     * @param ip LOCIC's IP address
     * @param subnetIp sub-network IP
     * @param subnetMask sub-network as IP mask belonging the LOCIC's domain
     * @param location location covered by the regarding central office (e.g. US.CA.Berkeley)
     */
    public Locic(InetAddress ip, String subnetIp, String subnetMask, String location) {
        this.ip         = ip;
        this.subnet     = new SubnetUtils(subnetIp, subnetMask);
        this.location   = location;
    }
    
    /**
     * Checks if an IP address belongs to the LOCIC's associated sub-network
     * @param ip IP
     * @return true if the given IP belongs to the sub-network
     */
    public boolean belongsToSubnet(InetAddress ip) {   
        String      ip_string   = ip.getHostAddress();
        SubnetInfo  subnet      = this.subnet.getInfo();    
        return subnet.isInRange(ip_string);        
    }
    
    /**
     * @return returns the IP address of the LOCIC
     */
    public InetAddress getIpAddr() {
        return this.ip;
    }
    
    /**
     * @return returns a String with the IP address of the LOCIC
     */
    public String getIpString() {
        return this.ip.getHostAddress();
    }
    
    public String getLocation() {
        return this.location;
    }

} // class
