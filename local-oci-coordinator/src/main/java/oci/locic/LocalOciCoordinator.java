/**
 * 
 */
package oci.locic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import oci.lib.GlobalConstants;
import oci.lib.ServiceNameEntry;
import oci.lib.ServiceNameRegistration;
import oci.lib.ServiceNameResolver;
/**
 * The LocalOciCoordinator class implements the Local OCI Coordinator.
 * 
 * @author Marc Koerner
 */
public class LocalOciCoordinator {
	
    static final Logger			LOGGER			= Logger.getLogger(LocalOciCoordinator.class.getName());
    static final int			SOCKET_TIMEOUT	= GlobalConstants.SOCKET_TIMEOUT;

    /**
	 * vector with discovery service name to IP mapping entries
	 */
    static Vector<ServiceNameEntry> serviceList = new Vector<ServiceNameEntry>();

	/**
	 * Local OCI Coordinator main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// set logging output to console
		Handler handlerObj = new ConsoleHandler();
		handlerObj.setLevel(Level.ALL);
		LOGGER.addHandler(handlerObj);
		LOGGER.setLevel(Level.ALL);
		LOGGER.setUseParentHandlers(false);
		
		LOGGER.info("Local OCI Coordinator started");
		
		// add mock echo edge service to service list
		ServiceNameEntry echoService = null;
		try {
			echoService = new ServiceNameEntry("mockService", InetAddress.getByName(ServiceNameResolver.IPADDRESS));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		// add mock service static entry
		LocalOciCoordinator.serviceList.add(echoService);
		
		try {
			// start service registration thread
			LOGGER.info("Try to open OCI name registraion port (" + ServiceNameRegistration.PORT + ")");
			ServerSocket serviceRegistrationSocket = new ServerSocket(ServiceNameRegistration.PORT);
			LOGGER.info("Successful");
			ServiceNameRegistrationThread serviceRegistrationWorker = new ServiceNameRegistrationThread(serviceRegistrationSocket);
			serviceRegistrationWorker.start();
			
			// start resource and orchestration manager thread
			LOGGER.info("Try to start RnO Manager communication module listening on port (" + 
					ResourceManagerCommunicationThread.PORT + ")");
			ServerSocket resourceManagerSocket = new ServerSocket(ResourceManagerCommunicationThread.PORT);
			LOGGER.info("Successful");
			ResourceManagerCommunicationThread resourceManagerWorker = new ResourceManagerCommunicationThread(resourceManagerSocket);
			resourceManagerWorker.start();
			
			// start service resolver thread
			LOGGER.info("Try to open a OCI name resolution port (" + ServiceNameResolver.PORT + ")");
			ServerSocket serviceResolverSocket = new ServerSocket(ServiceNameResolver.PORT);
			LOGGER.info("Successful");
			ServiceNameResolverThread serviceResolverWorker = new ServiceNameResolverThread(serviceResolverSocket, resourceManagerWorker);
			serviceResolverWorker.start();
			
			// read from cmd and wait for command EXIT or statistics
			InputStreamReader	inputStreamReader	= new InputStreamReader(System.in);
			BufferedReader		stdIn		 		= new BufferedReader(inputStreamReader);					
			String inputLine;
			while ((inputLine = stdIn.readLine()) != null) {
				
				// Exit
				if(inputLine.equals("exit")) {
					// shut down all serverSockets and wait until worker threads terminate (registration + resolver)
					serviceRegistrationSocket.close(); // interrupts accept() method within thread implementation
					serviceRegistrationWorker.join();
					
					serviceResolverSocket.close();
					serviceResolverWorker.join();
					
					resourceManagerWorker.disconnect();
					resourceManagerSocket.close();
					resourceManagerWorker.join();
					break;
				} //if exit
				
				// List service entries
				if(inputLine.equals("ls")) {
					for(int i = 0; i < LocalOciCoordinator.serviceList.size(); i++) {
						System.out.println(serviceList.get(i).toString());
					}
				} // if list
				
			} // while
			
		} catch(Exception error) {
			
			LOGGER.warning(error.getMessage());
			// LOGGER.warning(error.getStackTrace().toString());
			
		} // try - catch
		
		LOGGER.info("Local OCI Coordinator stopped");

	} // main

} // class LocalOciCoordinator
