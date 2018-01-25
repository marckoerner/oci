/**
 * 
 */
package oci.locic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import oci.lib.ServiceNameEntry;

/**
 * @author Marc Koerner
 * 
 * The ServiceNameResolverWorkerThread class implements and worker
 * thread which deals with a single client name resolution requests.
 */
public class ServiceNameResolverWorkerThread extends Thread {
	
	private ResourceManagerCommunicationThread	resourceManager			= null;
	private Socket								serviceResolverClient	= null;
	
	public ServiceNameResolverWorkerThread(Socket serviceResolverClient, ResourceManagerCommunicationThread	resourceManager) {
		this.serviceResolverClient	= serviceResolverClient;
		this.resourceManager		= resourceManager;
	}
		
	public void run() {
			
		try {			
			this.serviceResolverClient.setSoTimeout(LocalOciCoordinator.SOCKET_TIMEOUT);
			this.serviceResolverClient.setTcpNoDelay(true);
			
			// create object streams (later UDP set/get implementation)
			ObjectOutputStream	oos = new ObjectOutputStream(this.serviceResolverClient.getOutputStream());
			ObjectInputStream	ois = new ObjectInputStream(this.serviceResolverClient.getInputStream());
			
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
	
		} catch(Exception error) {
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
		} // try - catch
		
	} // run
	
} // class
