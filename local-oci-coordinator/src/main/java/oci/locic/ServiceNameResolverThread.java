/**
 * 
 */
package oci.locic;

import java.net.ServerSocket;

/**
 * @author marc
 *
 */
public class ServiceNameResolverThread extends Thread {
	
	private ServerSocket	serverSocket	= null;
	
	
	public ServiceNameResolverThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	
	@Override 
	public void run() {
		
		
		
	} // run
	

} // class ServiceNameResolverThread
