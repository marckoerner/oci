/**
 * 
 */
package oci.locic;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * The ServiceNameResolverThread class implements and worker thread for dealing with client name resolution requests
 * 
 * @author Marc Koerner
 */
public class ServiceNameResolverThread extends Thread {
	
	private ServerSocket						serverSocket	= null;
	private ResourceManagerCommunicationThread	resourceManager	= null;
	
	public ServiceNameResolverThread(ServerSocket serverSocket, ResourceManagerCommunicationThread resourceManager) {
		this.serverSocket		= serverSocket;
		this.resourceManager	= resourceManager;
	}
	
	@Override 
	public void run() {
		
		Socket serviceResolverClient = null;
		
		try {
			while(true) {
				LocalOciCoordinator.LOGGER.info("Waiting for ServiceNameResolver registration");
				// blocking accept is ended when client connects or LOCIC main closes the server socket => exception
				serviceResolverClient = this.serverSocket.accept();
				LocalOciCoordinator.LOGGER.info("ServiceNameResolver client connected");
				ServiceNameResolverWorkerThread worker = new ServiceNameResolverWorkerThread(serviceResolverClient, this.resourceManager);
				worker.start();
			} // while
		
		} catch(Exception error) {
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
		}
		
		LocalOciCoordinator.LOGGER.info("Thread stopped");
		
	} // run
	
} // class ServiceNameResolverThread
