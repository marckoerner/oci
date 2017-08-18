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
 * The ServiceNameResolverThread class implements and worker thread for the providing client requests with the edge service ip address
 * 
 * @author Marc Koerner
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
				// blocking accept is ended when client connects or LOCIC main closes the server socket => exception
				serviceResolverClient = this.serverSocket.accept();
				LocalOciCoordinator.LOGGER.info("ServiceNameResolver client connected");

				// create object streams (later UDP set/get implementation)
				ObjectOutputStream	oos = new ObjectOutputStream(serviceResolverClient.getOutputStream());
				ObjectInputStream	ois = new ObjectInputStream(serviceResolverClient.getInputStream());
							
				ServiceNameEntry serviceNameEntry = (ServiceNameEntry) ois.readObject();
				if(serviceNameEntry == null) break;
				
				for(int i = 0; i < LocalOciCoordinator.serviceList.size(); i++) {
					if(LocalOciCoordinator.serviceList.get(i).getServiceName().equals(serviceNameEntry.getServiceName())) {
						
						// TODO how to handle duplicated entries - currently client receives all matching service entries
						if(serviceNameEntry.getIpAddress() == null) { // not necessary safety check
							oos.writeObject(LocalOciCoordinator.serviceList.get(i));
							oos.flush();
							LocalOciCoordinator.LOGGER.info("Service name entry sent");

						}
						
					} else {
						// signal name resolver client that there is no service entry
						// oos.writeObject(null);
						LocalOciCoordinator.LOGGER.info("No service name entry found");
					}

				} // for
				
				LocalOciCoordinator.LOGGER.info("Close service name resolver connection");
				ois.close();
				oos.close();
				
			} // while
		
		} catch(Exception error) {
			
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
			
		} // try - catch
		
	} // run
	
} // class ServiceNameResolverThread
