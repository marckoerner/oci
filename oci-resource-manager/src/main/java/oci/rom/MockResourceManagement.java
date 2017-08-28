package oci.rom;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MockResourceManagement implements GenericResourceManagment {
	
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
		try {
			return InetAddress.getByAddress(new byte[] {127, 0, 0, 1});
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean resourcesAvailable() {
		return true;
	}

}
