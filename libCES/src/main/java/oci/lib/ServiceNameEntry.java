/**
 * 
 */
package oci.lib;

import java.net.InetAddress;

/**
 * The ServiceNameEntry class maps a service name to an IP address
 * 
 * @author marc
 */
public class ServiceNameEntry {
	
	private String		serviceName	= null;
	private InetAddress ip			= null;
	
	
	public ServiceNameEntry(String serviceName, InetAddress ip) {
		this.serviceName	= serviceName;
		this.ip				= ip;	
	}
	
	public InetAddress getIpAddress() {
		return this.ip;
	}
	
	public String getServiceName() {
		return this.serviceName;
	}
	
	@Override
	public String toString() {
		String returnValue = "";
		returnValue += this.serviceName;
		returnValue += this.ip.toString();
		return returnValue;
	}
	
	public boolean equals(ServiceNameEntry entry) {
		boolean returnValue = false;
		if(this.serviceName == entry.getServiceName()
				&& this.ip.equals(entry.getIpAddress())) returnValue = true;
		return returnValue;
	}

} // class ServiceNameEntry