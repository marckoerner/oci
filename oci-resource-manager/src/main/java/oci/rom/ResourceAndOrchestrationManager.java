/**
 * 
 */
package oci.rom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import oci.lib.GlobalConstants;
import oci.locic.ResourceManagerCommunicationThread;
import oci.rom.adapter.MininetResourceManagement;

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
	
    public	final static Logger			LOGGER			= Logger.getLogger(ResourceAndOrchestrationManager.class.getName());
    public	final static String			LOCIC_IP		= GlobalConstants.LOCALHOST_STRING;
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
		
		// parse command line arguments
		String locic_ip = null;
		if(args.length <= 1) {
			if(args.length == 1) locic_ip = args[0];
		} else {
			System.out.println("Usage: java -jar RnOM.jar [LOCIC IP]");
			System.out.println("Starts the OCI RnOM adapter and tries to connect to");
			System.out.println("to the LOCIC at [LOCIC IP]");
			System.exit(0);
		}

		/**
		 * MockResourceManagement can later on be replaced with the regarding resource management 
		 * implementation like E2, Mininet, OpenStack, ...
		 **/
		// GenericResourceManagement resourceManagement = new MockResourceManagement("mockResourceManager");
		GenericResourceManagement resourceManagement = new MininetResourceManagement();
		
		try {
			// connect to local OCI coordinator
			LOGGER.info("Try to connect to LOCIC");
			if(locic_ip == null) locic_ip			= LOCIC_IP;
			Socket locic							= new Socket(InetAddress.getByName(locic_ip), LOCIC_PORT);	
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
					// TODO send "exit" message or null to LOCIC
					
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
