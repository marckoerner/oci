package oci.rom;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import oci.lib.ServiceNameEntry;
import oci.lib.ServiceNameRegistration;

/**
 * This thread handles the Local-OCI-Coordinator communication
 * 
 * @author Marc Koerner
 */
public class LocicCommunicationThread extends Thread {
	
	private Socket						locic			= null;
	private GenericResourceManagement	resourceManager = null;
	
	LocicCommunicationThread(Socket locic, GenericResourceManagement resourceManager) {
		this.locic				= locic;
		this.resourceManager	= resourceManager;
	}
	
	@Override
	public void run() {
		//TODO redesign RnOM <-> LOCIC communication: stop edge service!?
		
		String serviceName = null;
		
		if(this.locic == null) return;
				
		ResourceAndOrchestrationManager.LOGGER.info("LocicCommunicationThread started");
		
		try {
			// create object streams (later UDP set/get implementation)
			ObjectOutputStream	oos = new ObjectOutputStream(this.locic.getOutputStream());
			ObjectInputStream	ois = new ObjectInputStream(this.locic.getInputStream());
			
			// main loop
			while(true) {
				
				// wait on instructions from LOCIC
				serviceName = (String) ois.readObject();
				
				if(serviceName == null) {
					// LOCIC sends null before shutting down the communication
					ResourceAndOrchestrationManager.LOGGER.info("LOCIC signaled end of communication");
					oos.writeBoolean(false);
					oos.flush();
					break;
				}
				
				ResourceAndOrchestrationManager.LOGGER.info("Verify availability of resources");
				if(resourceManager.resourcesAvailable()) {
					ResourceAndOrchestrationManager.LOGGER.info("Resources are available - try to start edge service");

					if(resourceManager.startEdgeService(serviceName)) {
						
						// getAddress
						InetAddress edge_service_ip = resourceManager.getEdgeServiceIP(serviceName);
						if(edge_service_ip != null) {
							
							// try to register edge service @ LOCIC
							int serviceKey = ServiceNameRegistration.registerEdgeService(serviceName, InetAddress.getByName("localhost"));
							if(serviceKey == ServiceNameEntry.NO_KEY) {
								ResourceAndOrchestrationManager.LOGGER.warning("Service Name Registration failed");
								// break;
							} else {
								ResourceAndOrchestrationManager.LOGGER.info("Service Name Registration successful");
								oos.writeBoolean(false);
							}
							
						} else {
							ResourceAndOrchestrationManager.LOGGER.warning("No edge service IP information");
							oos.writeBoolean(false);
						}
						
						oos.writeBoolean(true);
						ResourceAndOrchestrationManager.LOGGER.info("Edge Service " + serviceName + " started");
					} else {
						oos.writeBoolean(false);
						ResourceAndOrchestrationManager.LOGGER.info("Edge Service " + serviceName + " not started due to unknown reason");
					}
					oos.flush();
					
				} else {
					ResourceAndOrchestrationManager.LOGGER.info("No resource available");
				}
				
			} // while loop
			
		} catch(Exception error) {
			ResourceAndOrchestrationManager.LOGGER.warning(error.getMessage());
		}
		
		ResourceAndOrchestrationManager.LOGGER.info("LocicCommunicationThread closed");
		
	} // run method

}
