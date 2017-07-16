/**
 * 
 */
package oci.lib;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author marc
 *
 */
public class ServiceNameResolver {
	
	// localhost
	private final static byte[]			IPADDRESS		= {(byte) 127, (byte) 0, (byte) 0, (byte) 1};
	public	final static int			PORT			= 5533; // OCI name service port (DNS 53)

	
	
	public final static InetAddress getEdgeServiceIpAddress(String serviceName) {
		
		InetAddress ip = null;
		
		// get Edge Service IP address via OCI name resolution protocol (UDP)
		try {
			ip = InetAddress.getByAddress(IPADDRESS);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ip;
		
		
	} // getEdgeServiceIpAddress

}
