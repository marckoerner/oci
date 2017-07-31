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
	
    private static final Logger LOGGER = Logger.getLogger(LocalOciCoordinator.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LOGGER.info("Local OCI Coordinator started");
		
		
		
		// wait for edge service IP registration
			// covered by pre-implemented DiscoveryService template?
				// name and ip registration 
		
		// wait for client nameService request 
			// send Edge service ip address by name lookup
		
		/////////////////// would it make sense to separate registration and lookup???
		
		
		// add mock echo edge service to service list
		ServiceNameEntry echoService = null;
		try {
			echoService = new ServiceNameEntry("mockService", InetAddress.getByAddress(ServiceNameResolver.IPADDRESS));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Vector<ServiceNameEntry> serviceList = new Vector<ServiceNameEntry>();
		serviceList.add(echoService);
		
		try {
			LOGGER.info("Try to open a server socket OCI name resolution port (" + ServiceNameResolver.PORT + ")");
			ServerSocket serviceResolverSocket = new ServerSocket(ServiceNameResolver.PORT);
			LOGGER.info("Try to open a server socket OCI name resolution port (" + ServiceNameRegistration.PORT + ")");
			ServerSocket serviceRegistrationSocket = new ServerSocket(ServiceNameRegistration.PORT);
	        

		// create worker threads for NameServiceResolver and NameServiceRegister
		// write / read information from service vector
		// while main-loop wait for console command to exit / print stats 
		
		
		/*
		
		// main loop
		String inputLine;
        while ((inputLine = in.readLine()) != null) {
            out.println(inputLine);
            System.out.println(inputLine);
            if(inputLine.equals("exit")) return;
        } // while
		
		*/


			LOGGER.info("Waiting for ServiceNameResolver request");
			Socket serviceResolver = serviceResolverSocket.accept();
			LOGGER.info("ServiceNameResolver client connected");
	
			LOGGER.info("Waiting for DiscoveryService registration");
			Socket serviceRegistration = serviceResolverSocket.accept();
			LOGGER.info("ServiceNameRegistration client connected");
		
			// create object streams (later UDP set/get implementation)
			ObjectInputStream	ois = new ObjectInputStream(serviceResolver.getInputStream());
			ObjectOutputStream	oos = new ObjectOutputStream(serviceResolver.getOutputStream());
			
		} catch(Exception error) {
			
			LOGGER.warning(error.getMessage());
			LOGGER.warning(error.getStackTrace().toString());
			
		} // try - catch
	
		
		

	} // main

} // class LocalOciCoordinator
