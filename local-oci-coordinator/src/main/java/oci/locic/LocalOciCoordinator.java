/**
 * 
 */
package oci.locic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Logger;

import oci.lib.ServiceNameEntry;
import oci.lib.ServiceNameRegistration;
import oci.lib.ServiceNameResolver;
/**
 * @author marc
 *
 */
public class LocalOciCoordinator {
	
    static final Logger LOGGER = Logger.getLogger(LocalOciCoordinator.class.getName());
	// create vector with discovery service name to IP entries
    static Vector<ServiceNameEntry> serviceList = new Vector<ServiceNameEntry>();


	/**
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
			ServiceNameRegistrationThread serviceRegistration = new ServiceNameRegistrationThread(serviceRegistrationSocket);
			serviceRegistration.start();
			
			// start service resolver thread
			LOGGER.info("Try to open a server socket OCI name resolution port (" + ServiceNameResolver.PORT + ")");
			ServerSocket serviceResolverSocket = new ServerSocket(ServiceNameResolver.PORT);
			LOGGER.info("Successful");
			ServiceNameResolverThread serviceResolver = new ServiceNameResolverThread(serviceResolverSocket);
			
			// create worker threads for NameServiceResolver and NameServiceRegister
			// write / read information from service vector
			// while main-loop wait for console command to exit / print stats
			
					
			// read from cmd and wait for command EXIT
			InputStreamReader	inputStreamReader	= new InputStreamReader(System.in);
			BufferedReader		stdIn		 		= new BufferedReader(inputStreamReader);					
			String inputLine;
			while ((inputLine = stdIn.readLine()) != null) {
				System.out.println(inputLine);
				if(inputLine.equals("exit")) {
					// shut down all sockets and wait until worker threads terminate (registration + resolver)
					serviceRegistrationSocket.close(); // interrupts accept() method within thread implementation
					serviceRegistration.join();
					serviceResolverSocket.close();
					serviceResolver.join();
					return;
				}
			} // while
			
			// workerThread.stop
			// serviceRegistrationSocket.close(); // 
			// workerThread.join();
			
			
		} catch(Exception error) {
			
			LOGGER.warning(error.getMessage());
			LOGGER.warning(error.getStackTrace().toString());
			
		} // try - catch
	
		
		

	} // main

} // class LocalOciCoordinator
