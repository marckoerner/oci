/**
 * 
 */
package oci.rom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * The Manager class implements the resource and orchestration manager. It 
 * provides a simple socket interface and protocol for local OCI
 * coordinator in order to communicate with the actual underlying 
 * resource management system.
 * 
 * @author Marc Koerner
 *
 */
public class ResourceAndOrchestrationManager {
	
    final static Logger			LOGGER			= Logger.getLogger(ResourceAndOrchestrationManager.class.getName());
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LOGGER.info("Resource and orchestration manager started");

		// MockResourceManagement can later on be replaced with the regarding resource management implementation (E2, Mininet, OpenStack, ...)
		GenericResourceManagement resourceManagement = new MockResourceManagement("mockResourceManager");	
	
		try {
			// connect to local OCI coordinator and start communication thread
			Socket locic							= new Socket();
			Thread locicCommunication				= new LocicCommunicationThread(locic, resourceManagement);
			
			InputStreamReader	inputStreamReader	= new InputStreamReader(System.in);
			BufferedReader		stdIn		 		= new BufferedReader(inputStreamReader);
			
			// main loop
			String inputLine;
			while ((inputLine = stdIn.readLine()) != null) {
				// System.out.println(inputLine);
				
				// Exit
				if(inputLine.equals("exit")) {
					// close socket and wait for thread 
					locic.close();
					locicCommunication.join();
					break;
				} //if exit
				
				
			} // while
		
		} catch(Exception error) {
			LOGGER.warning(error.getMessage());
		}
		
		
		LOGGER.info("Resource and orchestration manager stopped");
		
	} // main

} // class Manager
