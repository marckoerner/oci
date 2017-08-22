/**
 * 
 */
package oci.lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Marc Koerner
 *
 */
public class ServiceNameRegistration {
	
	public final static byte[]			IPADDRESS		= {(byte) 127, (byte) 0, (byte) 0, (byte) 1}; 	// LOCIC IP (first iteration)
	public final static int				PORT			= ServiceNameResolver.PORT + 1; // Port 5534
	
    private final static int			SOCKET_TIMEOUT	= 5000; // 5 seconds timeout
	
	/**
	 * This method registers an edge service an the local OCI coordinator
	 * @param serviceName Name of the edge service
	 * @param ip IP address of the edge service
	 * @return result of the registration
	 */
	public final static boolean registerEdgeService(String serviceName, InetAddress ip) {
		
		ServiceNameEntry edgeServiceEntry = new ServiceNameEntry(serviceName, ip);
		return registerEdgeService(edgeServiceEntry);
	}
	
	/**
	 * This method registers an edge service an the local OCI coordinator
	 * @param serviceNameEntry Service name entry of the edge service
	 * @return result of the registration
	 */
	public final static boolean registerEdgeService(ServiceNameEntry serviceNameEntry) {
		
		boolean ret = true;
		
		try {
			Socket locicSocket = new Socket(InetAddress.getByAddress(IPADDRESS), PORT);
			locicSocket.setSoTimeout(SOCKET_TIMEOUT);
			
			// create object streams
			ObjectOutputStream	oos = new ObjectOutputStream(locicSocket.getOutputStream());
			ObjectInputStream	ois = new ObjectInputStream(locicSocket.getInputStream());			
			
			oos.writeObject(serviceNameEntry);
			oos.flush();
			
			ois.close();
			oos.close();
			
			ret = true;
						
		} catch (IOException e) {
			e.printStackTrace();
			ret = false;
		}
		
		return ret;
	}
	
	/**
	 * Registers a service with IP address of the first local IP address
	 * 
	 * @param serviceName Name of the service
	 * @return
	 */
	public boolean registerEdgeService(String serviceName) {
		
		return true;
	}
	
	

} // class ServiceNameRegistration
