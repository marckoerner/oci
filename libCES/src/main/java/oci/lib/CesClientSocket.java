package oci.lib;

import java.net.UnknownHostException;


//for static implementation
import java.net.Socket;

/**
 * @author      Marc Koerner <koerner@icsi.berkeley.edu>
 * @version     0.1
 */
public class CesClientSocket extends CesSocket{
	
	private String serviceName = new String();
	
	
	public CesClientSocket(String serviceName) throws UnknownHostException {
		CesSocket clientSocket = new CesSocket(SocketType.CLIENT);
		this.setServiceName(serviceName);
	}
	
	
	
	
	
	
	/**
	 * @return if connection was successful 
	 */
	public boolean connect() {
		// change type to DiscoverService IP address???
		return false;
	}
	
	/**
	 * @return connect to edge service 
	 */
	public boolean connect(String serviceName) {
		// change type to DiscoverService IP address???
		this.setServiceName(serviceName);
		return false;
	}



	/**
	 * @return serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param set serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	
	
	// static implementation //
	///////////////////////////
	public static Socket getEdgeDiscoveryServiceSocketConnection() {
		
		// get discovery service if available???
		
		// get edge service IP address
		Socket clientSocket = new Socket();

		return clientSocket;
	}
	
public static Socket getEdgeDiscoveryServiceSocketConnection(String serviceName) {
		
		// get discovery service if available???
		
		// get edge service IP address
		Socket clientSocket = new Socket();

		return clientSocket;
	}
	
	public static Socket getEdgeServiceSocketConnection() {
		
		// get discovery service if available???
		
		// get edge service IP address
		Socket clientSocket = new Socket();

		return clientSocket;
	}
	
	public static Socket getEdgeServiceSocketConnection(String serviceName) {
		
		// get discovery service if available???
		
		// get edge service IP address
		Socket clientSocket = new Socket();

		return clientSocket;
	}
	
	/**
	 * static implementation with datagram socket
	 */


} // CesClientSocket
