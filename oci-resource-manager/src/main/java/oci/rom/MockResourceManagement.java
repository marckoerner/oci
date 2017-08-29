package oci.rom;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Mock resource management implementation.
 * @author Marc Koerner
 */

public class MockResourceManagement implements GenericResourceManagement {
	
	private String edgeServiceFileName = null;
	
	public MockResourceManagement(String appFileName) {
		this.edgeServiceFileName = appFileName;
	}

	public boolean startEdgeService() {
		System.out.println("Edge Service " + this.edgeServiceFileName + " started");
		return false;
	}

	public boolean stopEdgeService() {
		System.out.println("Edge Service " + this.edgeServiceFileName + " stopped");
		return false;
	}

	public InetAddress getEdgeServiceIP() {
		InetAddress ip = null;
		try {
			ip = InetAddress.getByAddress(new byte[] {127, 0, 0, 1});
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("Edge Service " + this.edgeServiceFileName + " IP address = " + ip.toString());
		return ip;
	}

	public boolean resourcesAvailable() {
		return true;
	}

}
