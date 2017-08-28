/**
 * 
 */
package oci.rom;

import java.net.InetAddress;

/**
 * Generic interface for the implementation of a particular resource management environment
 * @author Marc Koerner
 */
public interface GenericResourceManagment {
	
	/**
	 * Validates if sufficient resources are available in order to start a new instance of a given edge service
	 * @return true if resources are available
	 */
	public boolean resourcesAvailable();
	
	/**
	 * Starts and bootstraps the give edge service
	 * @return true if startup was successful
	 */
	public boolean startEdgeService();
	
	/**
	 * Stops the edge service
	 * @return true if no error occurred
	 */
	public boolean stopEdgeService();
	
	/**
	 * Obtains edge service IP address 
	 * @return returns the IP address of the edge service
	 */
	public InetAddress getEdgeServiceIP(); 
	
	// public boolean edgeServiceIsRunning();
			
} // interface
