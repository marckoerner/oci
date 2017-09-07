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

				// create object streams (later UDP set/get implementation)
				ObjectOutputStream	oos = new ObjectOutputStream(serviceResolverClient.getOutputStream());
				ObjectInputStream	ois = new ObjectInputStream(serviceResolverClient.getInputStream());
							
				ServiceNameEntry serviceNameEntry = (ServiceNameEntry) ois.readObject();
				if(serviceNameEntry == null) {
					LocalOciCoordinator.LOGGER.warning("NULL pointer from read on socket");
					
				} else {
					
					boolean serviceNameEntryAvailable = false;
					for(int i = 0; i < LocalOciCoordinator.serviceList.size(); i++) {
						
						if(LocalOciCoordinator.serviceList.get(i).getServiceName().equals(serviceNameEntry.getServiceName())
								&& serviceNameEntry.getIpAddress() == null) { // not necessary safety check
							
							serviceNameEntryAvailable = true;
							oos.writeObject(LocalOciCoordinator.serviceList.get(i));
							oos.flush();
							LocalOciCoordinator.LOGGER.info("Service name entry sent");
							break;
						} 
					} // for
						
					if(!serviceNameEntryAvailable) {
						LocalOciCoordinator.LOGGER.info("No service name entry found");
						
						// call resource manager / ask to start the not yet available service
						this.resourceManager.serviceRequest(serviceNameEntry.getServiceName());
					}
				
				} // if - else
				
				LocalOciCoordinator.LOGGER.info("Close service name resolver connection");
				ois.close();
				oos.close();
				serviceResolverClient.close();
				
			} // while
		
		} catch(Exception error) {
			
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
			
		} // try - catch
		
	} // run
	
} // class ServiceNameResolverThread
