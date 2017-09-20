/**
 * 
 */
package oci.gocic.types;

import java.net.InetAddress;

/**
 * @author Torsten Runge
 *
 */
public class LocalCoordinator {
	private int id;
    private InetAddress ip;
	private String location;

	public LocalCoordinator(int id, InetAddress ip, String location) {
//		int idTemp = GlobalOciCoordinator.localCoordinators.size() + 1;
		
		// generate unique id
//		while (GlobalOciCoordinator.localCoordinators.get(idTemp)!= null)
//		{
//			idTemp++;
//		}

		this.id = id;
		this.ip = ip;
		this.location = location;
	}
	
    
	public int getId() {
		return id;
	}	

	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
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
	
	// JUNG2 makes good use of it for labeling in visualization
	public String toString() {
        return ""+id;
    }        
}