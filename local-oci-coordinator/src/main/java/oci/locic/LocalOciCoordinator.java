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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LOGGER.info("Local OCI Coordinator started");
		
		// create vector with discovery service name to IP entries
	    Vector<ServiceNameEntry> serviceList = new Vector<ServiceNameEntry>();
		
		// add mock echo edge service to service list
		ServiceNameEntry echoService = null;
		try {
			echoService = new ServiceNameEntry("mockService", InetAddress.getByAddress(ServiceNameResolver.IPADDRESS));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			serviceList.add(echoService);
		}
		
		// start service registration and resolver threads
		try {
			// open service name resolver socket server
			LOGGER.info("Try to open a server socket OCI name resolution port (" + ServiceNameResolver.PORT + ")");
			ServerSocket serviceResolverSocket = new ServerSocket(ServiceNameResolver.PORT);
			LOGGER.info("Successful");

			// open service name resolution socket server
			LOGGER.info("Try to open a server socket OCI name resolution port (" + ServiceNameRegistration.PORT + ")");
			ServerSocket serviceRegistrationSocket = new ServerSocket(ServiceNameRegistration.PORT);
			LOGGER.info("Successful");
			ServiceNameRegistrationThread serviceRegistration = new ServiceNameRegistrationThread(serviceRegistrationSocket);
			serviceRegistration.start();
	        
			LOGGER.info("Waiting for ServiceNameResolver request");
			Socket serviceResolver = serviceResolverSocket.accept();
			LOGGER.info("ServiceNameResolver client connected");
		
			// create object streams (later UDP set/get implementation)
			ObjectInputStream	ois = new ObjectInputStream(serviceResolver.getInputStream());
			ObjectOutputStream	oos = new ObjectOutputStream(serviceResolver.getOutputStream());
			
			// create worker threads for NameServiceResolver and NameServiceRegister
			// write / read information from service vector
			// while main-loop wait for console command to exit / print stats
			
					
			// create standard (CMD) i/o accessible objects
			InputStreamReader	inputStreamReader	= new InputStreamReader(System.in);
			BufferedReader		stdIn		 		= new BufferedReader(inputStreamReader);					
			
			String inputLine;
			while ((inputLine = stdIn.readLine()) != null) {
				// out.println(inputLine);
				System.out.println(inputLine);
				if(inputLine.equals("exit")) {
					// shut down all threads (registration + resolver)
					serviceRegistrationSocket.close(); // interrupts accept() method within thread implementation
					serviceRegistration.join();
					
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
