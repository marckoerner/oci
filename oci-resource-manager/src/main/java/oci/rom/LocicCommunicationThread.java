package oci.rom;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
		
		String serviceName = null;
		
		try {
			// create object streams (later UDP set/get implementation)
			ObjectOutputStream	oos = new ObjectOutputStream(this.locic.getOutputStream());
			ObjectInputStream	ois = new ObjectInputStream(this.locic.getInputStream());
			
			// main loop
			while(true) {
				
				// wait on instructions from LOCIC
				serviceName = (String) ois.readObject();
				Manager.LOGGER.info("Try to start edge service");
				
				if(resourceManager.resourcesAvailable()) {
					Manager.LOGGER.info("Resource are available");

					if(resourceManager.startEdgeService(serviceName)) {
						Manager.LOGGER.info("Edge Service " + serviceName + " started");
						oos.writeBoolean(true);
					} else {
						Manager.LOGGER.info("Edge Service " + serviceName + " not started due to unknown reason");
						oos.writeBoolean(false);
					}
					oos.flush();
					
				} else {
					Manager.LOGGER.info("No resource available");
				}
				
			}
			
			// wait for command 
				// switch - case 
					// start
						// return IP
					// stop
		} catch(Exception error) {
			Manager.LOGGER.warning(error.getMessage());
		}
		
	}

}
