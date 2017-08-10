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

} // class ServiceNameEntry
