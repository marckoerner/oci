
package oci.lib;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * The ServiceNameEntry class maps a service name to an IP address. An object of this class represents and edge service entry
 * @author Marc Koerner
 */
public class ServiceNameEntry implements Serializable {
	
	public	static final int 	NO_KEY				= -1;
	private static final long	serialVersionUID	= 1L;
	
	private String		serviceName	= null;
	private InetAddress ip			= null;
	private int         requests    = 0;
	private int			key			= NO_KEY;
		
	/**
	 * Constructor
	 * @param serviceName Name of the edge service
	 * @param ip IP address of the edge service
	 */
	public ServiceNameEntry(String serviceName, InetAddress ip) {
		this.serviceName	= serviceName;
		this.ip				= ip;	
	}
	
	/**
	 * @return IP address of the edge service
	 */
	public InetAddress getIpAddress() {
		return this.ip;
	}
	
	/**
	 * @returnService Name of the edge service
	 */
	public String getServiceName() {
		return this.serviceName;
	}
	
	/**
	 * @param key sets the registration key of an edge service entry. Should not be manipulated manually.
	 */
	public void setKey(int key) {
		this.key = key;
	}
	
	/**
	 * @return registration key of the edge service, if service is not registered yet it returns NO_KEY
	 */
	public int getKey() {
		return this.key;
	}
	
	/**
	 * Increases the request counter by 1
	 */
	public void newRequest() {
	    this.requests++;
	}
	
	/**
	 * @return returns the value of the request counter
	 */
	public int getRequests() {
	    return this.requests;
	}
	
	@Override
	public String toString() {
		String returnValue = this.key + ":";
		returnValue += this.serviceName + "@";
		returnValue += this.ip.toString();
		return returnValue;
	}
	
	public boolean equals(ServiceNameEntry entry) {
		boolean returnValue = false;
		if(		this.key				== entry.getKey()
				&& this.serviceName == entry.getServiceName()
				&& this.ip.equals(entry.getIpAddress())) returnValue = true;
		return returnValue;
	}

} // class ServiceNameEntry
