/**
 * 
 */
package oci.mock.client;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import oci.lib.CesClientSocket;
import oci.lib.ServiceNameResolver;


/**
 * @author marc
 *
 */
public class EchoClient {
	
	// OCI service name
	public final static String	SERVICE_NAME	= "mockService";
	public final static int		SERVICE_PORT	= 9292;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Socket clientSocket = null;
		
		try {
			// connect to edge service
			clientSocket = new Socket(ServiceNameResolver.getEdgeServiceIpAddress(SERVICE_NAME), SERVICE_PORT);
			
			// create i/o accessible objects
			PrintWriter		out	= new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader	in	= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (Exception error) {
			
		} // try - catch

	} // main

} // Client class
