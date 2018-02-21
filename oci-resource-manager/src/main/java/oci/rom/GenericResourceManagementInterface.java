/**
 * 
 */
package oci.rom;

import java.io.File;
import java.net.InetAddress;

/**
 * Generic interface for the implementation of a particular resource management environment
 * @author Marc Koerner
 */
public interface GenericResourceManagementInterface {
	
	/**
	 * Validates if sufficient resources are available in order to start a new instance of a given edge service
	 * @return true if resources are available
	 */
	public boolean resourcesAvailable();
	
	/**
	 * Validates if sufficient cpu, memory and disk resources are available in order to start a new 
	 * instance of a given edge service
	 * @param cpu normalized cpu performance value
	 * @param memory amount of memory in MB
	 * @param amount of disk in MB
	 * @return true if resources are available
	 */
	public boolean resourcesAvailable(int cpu, int memory, int disk);
	
	/**
	 * Validates if sufficient compute resources are available in order to start a new instance of a given edge service
	 * @param cpu_cores amount of cpu cores
	 * @param frequency minimum core frequency
	 * @return true if resources are available
	 */
	public boolean resourcesAvailable(int cpu_cores, int frequency);
	
	/**
	 * Starts and bootstraps the give edge service
	 * @param edgeServiceName name of the edgeServiceApplicationPackage
	 * @return true if startup was successful
	 */
	public boolean startEdgeService(String edgeServiceName);
	
	/**
	 * Starts and bootstraps the give edge service
	 * @param edgeServiceApplicationPackage EdgeServiceApplicationPackage (*.esap) file
	 * @return true if startup was successful
	 */
	public boolean startEdgeService(File edgeServiceApplicationPackage);
	
	/**
	 * Stops the edge service
	 * @return true if no error occurred
	 */
	public boolean stopEdgeService(String edgeServiceName);
	
	/**
	 * Obtains edge service IP address 
	 * @return returns the IP address of the edge service
	 */
	public InetAddress getEdgeServiceIP(String edgeServiceName); 
	
	/**
	 * Determines the execution status of an edge service
	 * @return true if service is running
	 */
	public boolean isEdgeServiceRunning();
			
} // interface
