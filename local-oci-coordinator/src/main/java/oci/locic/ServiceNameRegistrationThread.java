/**
 * 
 */
package oci.locic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import oci.lib.ServiceNameEntry;

/**
 * The ServiceNameRegistrationThread implements a worker thread which adds the edge service name to IP address mapping information to the GOCIC service entry list
 * 
 * @author marc
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
				ObjectOutputStream	oos = new ObjectOutputStream(serviceRegistrationClient.getOutputStream());
				ObjectInputStream	ois = new ObjectInputStream(serviceRegistrationClient.getInputStream());
				
				// obtain service entry and add it to LOCIC service entry vector
				ServiceNameEntry serviceNameEntry = (ServiceNameEntry) ois.readObject();
				
				if(serviceNameEntry != null) {
					
					// new edge service registration
					if(serviceNameEntry.getKey() == ServiceNameEntry.NO_KEY) {
						
						boolean serviceEntryNameAlreadyInUse = false;
						int key = ServiceNameEntry.NO_KEY;
						
						// check if service name is already in use before adding it to the service vector
						for(int i = 0; i < LocalOciCoordinator.serviceList.size(); i++) {
							if(LocalOciCoordinator.serviceList.get(i).getServiceName().equals(serviceNameEntry.getServiceName())) {
								serviceEntryNameAlreadyInUse = true;
								break;
							}
						}
						
						if(serviceEntryNameAlreadyInUse) {
							LocalOciCoordinator.LOGGER.info("Service entry: " + serviceNameEntry.toString() + " name already in use");
						} else {
							// generate random service entry key
							key = new Random().nextInt(Integer.MAX_VALUE); // generate int between 0 and MAX_INT
							serviceNameEntry.setKey(key);
							LocalOciCoordinator.serviceList.add(serviceNameEntry);
							LocalOciCoordinator.LOGGER.info("ServiceNameEntry received:" + serviceNameEntry.toString());
						}
						
						// send return value back to service registration client aka edge service
						oos.writeInt(key);
						oos.flush();
					}
					
					// edge service un-registration
					else {
						boolean ret = false;

						for(int i = 0; i < LocalOciCoordinator.serviceList.size(); i++) {
							ServiceNameEntry j = LocalOciCoordinator.serviceList.get(i);
							
							if(j.getKey() == serviceNameEntry.getKey() && j.getServiceName().equals(serviceNameEntry.getServiceName())) {
								// delete service entry 
								LocalOciCoordinator.serviceList.remove(i);
								LocalOciCoordinator.LOGGER.info("Service entry: " + j.toString() + " deleted");
								oos.writeInt(ServiceNameEntry.NO_KEY);
								oos.flush();
								ret = true;
							}
														
						} // for
						
						if(ret == false) {
							oos.writeInt(serviceNameEntry.getKey());
							oos.flush();
							LocalOciCoordinator.LOGGER.info("No matching service entry found");
						}
					}
					
				} else {
					LocalOciCoordinator.LOGGER.info("no ServiceNameEntry received - buffer error");
				}
				
				// close connection to client
				oos.close();
				ois.close();
				serviceRegistrationClient.close();
				LocalOciCoordinator.LOGGER.info("Client connection closed");
				
			} // while
		
		} catch(Exception error) {
			
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
			
		} // try - catch
		
	} // run	

} // class ServiceNameRegistrationThread
