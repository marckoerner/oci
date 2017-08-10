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
import java.util.logging.Logger;

import oci.lib.ServiceNameEntry;
import oci.lib.ServiceNameRegistration;
import oci.lib.ServiceNameResolver;
/**
 * The LocalOciCoordinator class implements the Local OCI Coordinator 
 * 
 * @author marc
 */
public class LocalOciCoordinator {
	
    static final Logger LOGGER = Logger.getLogger(LocalOciCoordinator.class.getName());
    /**
	 * vector with discovery service name to IP entries
	 */
    static Vector<ServiceNameEntry> serviceList = new Vector<ServiceNameEntry>();

	/**
	 * Local OCI Coordinator started main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		LOGGER.info("Local OCI Coordinator started");
		
		// create vector with discovery service name to IP entries
	    // Vector<ServiceNameEntry> serviceList = new Vector<ServiceNameEntry>();
		
		// add mock echo edge service to service list
		ServiceNameEntry echoService = null;
		try {
			echoService = new ServiceNameEntry("mockService", InetAddress.getByAddress(ServiceNameResolver.IPADDRESS));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LocalOciCoordinator.serviceList.add(echoService);
		}
		
		try {
			// start service registration thread
			LOGGER.info("Try to open a server socket OCI name resolution port (" + ServiceNameRegistration.PORT + ")");
			ServerSocket serviceRegistrationSocket = new ServerSocket(ServiceNameRegistration.PORT);
			LOGGER.info("Successful");
			ServiceNameRegistrationThread serviceRegistrationWorker = new ServiceNameRegistrationThread(serviceRegistrationSocket);
			serviceRegistrationWorker.start();
			
			// start service resolver thread
			LOGGER.info("Try to open a server socket OCI name resolution port (" + ServiceNameResolver.PORT + ")");
			ServerSocket serviceResolverSocket = new ServerSocket(ServiceNameResolver.PORT);
			LOGGER.info("Successful");
			ServiceNameResolverThread serviceResolverWorker = new ServiceNameResolverThread(serviceResolverSocket);
								
			// read from cmd and wait for command EXIT or statistics
			InputStreamReader	inputStreamReader	= new InputStreamReader(System.in);
			BufferedReader		stdIn		 		= new BufferedReader(inputStreamReader);					
			String inputLine;
			while ((inputLine = stdIn.readLine()) != null) {
				System.out.println(inputLine);
				if(inputLine.equals("exit")) {
					// shut down all serverSockets and wait until worker threads terminate (registration + resolver)
					serviceRegistrationSocket.close(); // interrupts accept() method within thread implementation
					serviceRegistrationWorker.join();
					serviceResolverSocket.close();
					serviceResolverWorker.join();
					return;
				}
			} // while
			
		} catch(Exception error) {
			
			LOGGER.warning(error.getMessage());
			// LOGGER.warning(error.getStackTrace().toString());
			
		} // try - catch
		
		LOGGER.info("LOCIC stopped");

	} // main

} // class LocalOciCoordinator
