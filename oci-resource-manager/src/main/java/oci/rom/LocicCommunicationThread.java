package oci.rom;

import java.net.Socket;

/**
 * This thread handles the Local-OCI-Coordinator communication
 * 
 * @author Marc Koerner
 */
public class LocicCommunicationThread extends Thread {
	
	private Socket locic = null;
	
	LocicCommunicationThread(Socket locic) {
		this.locic = locic;
	}
	
	@Override
	public void run() {
		
		// static connection??? to locic???
		
		// wait for command 
			// switch - case 
				// start
					// return IP
				// stop
		
		
	}

}
