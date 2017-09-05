/**
 * 
 */
package oci.gocic;

import java.net.InetAddress;

/**
 * @author Torsten Runge
 *
 */
public class LocalCoordinator {
	String name;
    private InetAddress ip;

	public LocalCoordinator(String name, InetAddress ip) {
		this.name = name;
		this.ip = ip;
	}
    
	/**
	 * @return the ip
	 */
	public InetAddress getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
}