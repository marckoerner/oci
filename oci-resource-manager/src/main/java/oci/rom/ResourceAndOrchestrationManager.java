/**
 * 
 */
package oci.rom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import oci.locic.ResourceManagerCommunicationThread;

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
    public	final static byte[]			LOCIC_IP		= {(byte) 127, (byte) 0, (byte) 0, (byte) 1}; 	// LOCIC IP
    public	final static int			LOCIC_PORT		= ResourceManagerCommunicationThread.PORT;
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// set logging output to console
		Handler handlerObj = new ConsoleHandler();
		handlerObj.setLevel(Level.ALL);
		LOGGER.addHandler(handlerObj);
		LOGGER.setLevel(Level.ALL);
		LOGGER.setUseParentHandlers(false);
		
		LOGGER.info("Resource and orchestration manager started");

		// MockResourceManagement can later on be replaced with the regarding resource management implementation (E2, Mininet, OpenStack, ...)
		GenericResourceManagement resourceManagement = new MockResourceManagement("mockResourceManager");	
	
		try {
			// connect to local OCI coordinator
			LOGGER.info("Try to connect to LOCIC");
			Socket locic							= new Socket(InetAddress.getByAddress(LOCIC_IP), LOCIC_PORT);
			LOGGER.info("Connected to LOCIC");
			
			//  Instantiate and start communication thread
			Thread locicCommunication				= new LocicCommunicationThread(locic, resourceManagement);
			locicCommunication.start();
						
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
