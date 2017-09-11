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
	int id;
    private InetAddress ip;

	public LocalCoordinator(InetAddress ip) {
		int idTemp = GlobalOciCoordinator.localCoordinators.size() + 1;
		
		// generate unique id
		while (GlobalOciCoordinator.localCoordinators.get(idTemp)!= null)
		{
			idTemp++;
		}

		this.id = idTemp;
		this.ip = ip;
	}
	
    
	public int getId() {
		return id;
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