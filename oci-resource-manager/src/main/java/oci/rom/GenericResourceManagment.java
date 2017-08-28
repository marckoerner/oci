/**
 * 
 */
package oci.rom;

import java.net.InetAddress;

/**
 * @author Marc Koerner
 *
 */
public interface GenericResourceManagment {
	
	public boolean resourcesAvailable();
		
	public boolean startEdgeService();
	
	public boolean stopEdgeService();
	
	public InetAddress getEdgeServiceIP(); 
	
	// public boolean edgeServiceIsRunning();
			
} // interface
