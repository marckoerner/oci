
package oci.lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class provides methods for the OCI service name resolution
 * 
 * @author Marc Koerner
 */
public class ServiceNameResolver {
	
	// localhost
	public final static byte[]			IPADDRESS		= {(byte) 127, (byte) 0, (byte) 0, (byte) 1}; 	// LOCIC IP (first iteration)
	public final static int				PORT			= 5533; // OCI name service port (DNS 53)
	
    private final static int			SOCKET_TIMEOUT	= 5000; // 5 seconds timeout

	
    /**
	 * Connects to the OCI name service and returns the IP of the requested edge service. 
	 * The method returns null if no service entry has been found.
	 * 
	 * @param serviceName Name of the edge service 
	 * @return IP address of the edge service or null if something went wrong
	 */
	public final static InetAddress getEdgeServiceIpAddress(String serviceName) {
		
		InetAddress ip = null;
		
		// TODO get Edge Service IP address via OCI name resolution protocol (UDP)
		try {			
			Socket locicSocket = new Socket(InetAddress.getByAddress(IPADDRESS), PORT);
			locicSocket.setSoTimeout(SOCKET_TIMEOUT);
			
			// create object streams (later UDP set/get implementation)
			ObjectOutputStream	oos = new ObjectOutputStream(locicSocket.getOutputStream());
			ObjectInputStream	ois = new ObjectInputStream(locicSocket.getInputStream());			
			
			// create ServiceNameEntry template and fetch information from name resolution service
			ServiceNameEntry edgeServiceEntry = new ServiceNameEntry(serviceName, null);
			oos.writeObject(edgeServiceEntry);
			oos.flush();
			edgeServiceEntry = (ServiceNameEntry) ois.readObject();
			
			ois.close();
			oos.close();
			locicSocket.close();
			
			ip = edgeServiceEntry.getIpAddress();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return ip;
		
	} // getEdgeServiceIpAddress

}
