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
	 * This method registers an edge service at the local OCI coordinator
	 * @param serviceName Name of the edge service
	 * @param ip IP address of the edge service
	 * @return registration key if successful and 0 if an error appeared
	 */
	public final static int registerEdgeService(String serviceName, InetAddress ip) {
		
		ServiceNameEntry edgeServiceEntry = new ServiceNameEntry(serviceName, ip);
		return registerEdgeService(edgeServiceEntry);
	}
	
	/**
	 * This method registers an edge service at the local OCI coordinator
	 * @param serviceNameEntry Service name entry of the edge service
	 * @return registration key if successful and ServiceNameEntry.NO_ID if an error appeared
	 */
	public final static int registerEdgeService(ServiceNameEntry serviceNameEntry) {
		
		// TODO: check method parameters for safety
		
		int ret = ServiceNameEntry.NO_KEY;
		
		if(		serviceNameEntry.getServiceName() == null 
				|| serviceNameEntry.getIpAddress() == null) return ret;
		
		try {
			Socket locicSocket = new Socket(InetAddress.getByAddress(IPADDRESS), PORT);
			locicSocket.setSoTimeout(SOCKET_TIMEOUT);
			
			// create object streams
			ObjectOutputStream	oos = new ObjectOutputStream(locicSocket.getOutputStream());
			ObjectInputStream	ois = new ObjectInputStream(locicSocket.getInputStream());			
			
			oos.writeObject(serviceNameEntry);
			oos.flush();
			
			int id = ois.readInt();
			serviceNameEntry.setKey(id); 
			
			ois.close();
			oos.close();
			
			ret = id;
						
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * This method un-registers an edge service at the local OCI coordinator
	 * @param serviceName Name of the edge service
	 * @param registrationKey return value of the registration aka registration key
	 * @return true if service was successful un-registered
	 */
	public final static boolean unregisterEdgeService(String serviceName, int registrationKey) {	
		
		ServiceNameEntry serviceNameEntry = new ServiceNameEntry(serviceName, null);
		serviceNameEntry.setKey(registrationKey);	
		return unregisterEdgeService(serviceNameEntry);	
	}
	
	/**
	 * This method un-registers an edge service at the local OCI coordinator
	 * @param serviceNameEntry Service name entry of the edge service
	 * @return true if service was successful un-registered
	 */
	public final static boolean unregisterEdgeService(ServiceNameEntry serviceNameEntry) {
		
		// TODO: check method parameters for safety
		
		boolean ret = true;
		if(serviceNameEntry.getKey() == registerEdgeService(serviceNameEntry)) ret = false;
		return ret;
	}
	

} // class ServiceNameRegistration
