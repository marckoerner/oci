/**
 * 
 */
package oci.locic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import oci.lib.ServiceNameEntry;

/**
 * @author marc
 *
 */
public class ServiceNameRegistrationThread extends Thread {
	
	private ServerSocket	serverSocket	= null;
	
	public ServiceNameRegistrationThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		
		Socket serviceRegistrationClient = null;
		
		try {
			
			while(true) {
				
				LocalOciCoordinator.LOGGER.info("Waiting for DiscoveryService registration");
				// blocking accept is ended when client connects or LOCIC main closes the server socket => exception
				serviceRegistrationClient = this.serverSocket.accept();
				LocalOciCoordinator.LOGGER.info("ServiceNameRegistration client connected");
				
				// create object streams for service name to IP resolution
				ObjectInputStream	ois = new ObjectInputStream(serviceRegistrationClient.getInputStream());
				//ObjectOutputStream	oos = new ObjectOutputStream(serviceRegistrationClient.getOutputStream());
				
				// obtain service entry and add it to LOCIC service entry vector
				ServiceNameEntry serviceNameEntry = (ServiceNameEntry) ois.readObject();
				LocalOciCoordinator.serviceList.add(serviceNameEntry);
				LocalOciCoordinator.LOGGER.info("ServiceNameEntry received:" + serviceNameEntry.toString());
				
				// close connection to client
				ois.close();
				serviceRegistrationClient.close();
				LocalOciCoordinator.LOGGER.info("Client connection closed");
				
			} // while
		
		} catch(Exception error) {
			
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
			
		} // try - catch
		
	} // run	

} // class ServiceNameRegistrationThread
