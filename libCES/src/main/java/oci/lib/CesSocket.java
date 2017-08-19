/**
 * 
 */
package oci.lib;

import java.net.UnknownHostException;



/**
 * @author      Marc Koerner <koerner@icsi.berkeley.edu>
 * @version     0.1
 */
public class CesSocket {
	
	/**
	 * IP address of the OCI name service or an global available address e.g. DNS root server. For testing 
	 */
	// private final static String 		IPADDRESS		= "x.y.z";
	// private final static byte[]			IPADDRESS		= {(byte) 192, (byte) 168, (byte) 1, (byte) 111};
	// private InetAddress					ipInetAddress;
	
	
	/**
	 * Socket type classifier
	 */
	public enum SocketType {
		CLIENT, EDGE, SERVER
	}
	
	
	
	
	
	public CesSocket() {
		
	}
	
	public CesSocket(SocketType type) throws UnknownHostException {
		// this.ipInetAddress = InetAddress.getByAddress(IPADDRESS);
	}
		
	

} // class CesSocket 
