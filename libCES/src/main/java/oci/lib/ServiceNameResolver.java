/**
 * 
 */
package oci.lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author marc
 *
 */
public class ServiceNameResolver {
	
	// localhost
	public final static byte[]			IPADDRESS		= {(byte) 127, (byte) 0, (byte) 0, (byte) 1}; 	// LOCIC IP (first iteration)
	public final static int				PORT			= 5533; // OCI name service port (DNS 53)

	
	
	public final static InetAddress getEdgeServiceIpAddress(String serviceName) {
		
		InetAddress ip = null;
		
		// get Edge Service IP address via OCI name resolution protocol (UDP)
		try {
			// ip = InetAddress.getByAddress(IPADDRESS); // hard coded dummy (deprecated)
			
			Socket locicSocket = new Socket(InetAddress.getByAddress(IPADDRESS), PORT);
			
			// create object streams (later UDP set/get implementation)
			ObjectInputStream	ois = new ObjectInputStream(locicSocket.getInputStream());
			ObjectOutputStream	oos = new ObjectOutputStream(locicSocket.getOutputStream());
			
			// create ServiceNameEntry template and fetch information from name resolution service
			ServiceNameEntry edgeServiceEntry = new ServiceNameEntry(serviceName, null);
			oos.writeObject(edgeServiceEntry);
			oos.flush();
			edgeServiceEntry = (ServiceNameEntry) ois.readObject();
			
			ois.close();
			oos.close();
			
			ip = edgeServiceEntry.getIpAddress();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return ip;
		
	} // getEdgeServiceIpAddress

}
