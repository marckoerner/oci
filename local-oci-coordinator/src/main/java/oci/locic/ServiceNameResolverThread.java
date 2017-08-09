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
public class ServiceNameResolverThread extends Thread {
	
	private ServerSocket	serverSocket	= null;
	
	
	public ServiceNameResolverThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	
	@Override 
	public void run() {
		
		Socket serviceResolverClient = null;
		
		try {
			
			while(true) {
				LocalOciCoordinator.LOGGER.info("Waiting for ServiceNameResolver registration");
				serviceResolverClient = this.serverSocket.accept();
				LocalOciCoordinator.LOGGER.info("ServiceNameResolver client connected");

				// create object streams (later UDP set/get implementation)
				ObjectInputStream	ois = new ObjectInputStream(serviceResolverClient.getInputStream());
				ObjectOutputStream	oos = new ObjectOutputStream(serviceResolverClient.getOutputStream());
				
				ServiceNameEntry serviceNameEntry = (ServiceNameEntry) ois.readObject();
				for(int i = 0; i < LocalOciCoordinator.serviceList.size(); i++) {
					if(LocalOciCoordinator.serviceList.get(i).getServiceName().equals(serviceNameEntry.getServiceName())) {
						
						if(serviceNameEntry.getIpAddress() == null) { // not necessary safety check
							oos.writeObject(LocalOciCoordinator.serviceList.get(i));
							oos.flush();

						}
						
					} else {
						// signal name resolver client that there is no service entry
						// oos.writeObject(null);
						LocalOciCoordinator.LOGGER.info("No service name entry found");
					}

				} // for
				
				ois.close();
				oos.close();
				
			} // while
		
		} catch(Exception error) {
			
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
			LocalOciCoordinator.LOGGER.warning(error.getStackTrace().toString());
			
		} // try - catch
		
	} // run
	
} // class ServiceNameResolverThread
