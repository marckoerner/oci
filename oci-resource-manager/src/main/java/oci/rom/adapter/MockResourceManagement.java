package oci.rom.adapter;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import oci.rom.GenericResourceManagementInterface;

/**
 * Mock resource management implementation based on the regarding generic interface
 * @author Marc Koerner
 */
public class MockResourceManagement implements GenericResourceManagementInterface {
	
	private String resourceManagementName	= null;
	//private String edgeServiceFileName		= null;
	
	public MockResourceManagement(String resourceManagementName) {
		this.resourceManagementName = resourceManagementName;
	}

	public boolean startEdgeService(String edgeServiceName) {
		boolean ret = false;
		if(edgeServiceName != null) {
			System.out.println("Edge Service " + edgeServiceName + " started");
			ret = true;
		}
		else System.out.println("Edge Service name = null - nothing to start");
		return ret;
	}

	public boolean stopEdgeService(String edgeServiceName) {
		boolean ret = false;
		if(edgeServiceName != null) {
			System.out.println("Edge Service " + edgeServiceName + " stopped");
			ret = true;
		}
		else System.out.println("Edge Service name = null - nothing to stop");
		return ret;
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
		System.out.println("Resources available");
		return true;
	}

	public boolean isEdgeServiceRunning() {
		System.out.println("Edge service not running");
		return false;
	}

	@Override
	public boolean resourcesAvailable(int cpu, int memory, int disk) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resourcesAvailable(int cpu_cores, int frequency) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startEdgeService(File edgeServiceApplicationPackage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resourcesAvailable(File edgeServiceApplicationPackage) {
		// TODO Auto-generated method stub
		return false;
	}

}
