/**
 * 
 */
package oci.locic;

import java.net.ServerSocket;
import java.net.Socket;

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
				serviceRegistrationClient = this.serverSocket.accept();
				LocalOciCoordinator.LOGGER.info("ServiceNameRegistration client connected");
				
				
				
			} // while
		
		} catch(Exception error) {
			
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
			LocalOciCoordinator.LOGGER.warning(error.getStackTrace().toString());
			
		} // try - catch
		
	} // run
	

} // class ServiceNameRegistrationThread
