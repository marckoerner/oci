package oci.rom;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Mock resource management implementation.
 * @author Marc Koerner
 */

public class MockResourceManagement implements GenericResourceManagement {
	
	private String resourceManagementName	= null;
	//private String edgeServiceFileName		= null;
	
	public MockResourceManagement(String resourceManagementName) {
		this.resourceManagementName = resourceManagementName;
	}

	public boolean startEdgeService(String edgeServiceName) {
		System.out.println("Edge Service " + edgeServiceName + " started");
		return false;
	}

	public boolean stopEdgeService(String edgeServiceName) {
		System.out.println("Edge Service " + edgeServiceName + " stopped");
		return false;
	}

	public InetAddress getEdgeServiceIP(String edgeServiceName) {
		InetAddress ip = null;
		try {
			ip = InetAddress.getByAddress(new byte[] {127, 0, 0, 1});
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("Edge Service " + edgeServiceName + " IP address = " + ip.toString());
		return ip;
	}

	public boolean resourcesAvailable() {
		return true;
	}

}
