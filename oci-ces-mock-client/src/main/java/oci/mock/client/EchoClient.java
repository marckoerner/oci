/**
 * 
 */
package oci.mock.client;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import oci.lib.ServiceNameResolver;


/**
 * @author marc
 *
 */
public class EchoClient {
	
	// Client wide logging
    private static final Logger LOGGER = Logger.getLogger(EchoClient.class.getName());
	
	// OCI service name
	public final static String	SERVICE_NAME	= "mockService";
	public final static int		SERVICE_PORT	= 9292;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LOGGER.info("EchoClient started");
		
		// try to establish connection between client and server
		LOGGER.info("Try to connect to server");
		Socket clientSocket = null;
		try {
			// connect to edge service using the OCI ServiceNameResolver (OCI CESlib)
			clientSocket = new Socket(ServiceNameResolver.getEdgeServiceIpAddress(SERVICE_NAME), SERVICE_PORT);
			// set timeout to 5 seconds
			clientSocket.setSoTimeout(5000);
			
			// create socket i/o accessible objects
			PrintWriter		out	= new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader	in	= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			LOGGER.info("connected");
			
			// create standard (CMD) i/o accessible objects
			InputStreamReader	inputStreamReader	= new InputStreamReader(System.in);
			BufferedReader		stdIn		 		= new BufferedReader(inputStreamReader);
			
			String fromServer	= null;
			String fromUser		= null;
			
			System.out.println("Type exit+Enter to stop the program");
			
			// server communication loop
			while ((fromUser = stdIn.readLine()) != null) {
				
				if (fromUser != null) {
			        System.out.println("Client: " + fromUser);
			        out.println(fromUser);
			    }
				
				if((fromServer = in.readLine()) != null) {
					System.out.println("Server: " + fromServer);
				}
			    
			    if (fromServer.equals("exit")) {
			    	System.out.println("Bye, bye!");
			    	break;
			    }

			} // while
			
		} catch (Exception error) {
			LOGGER.warning(error.toString());
			LOGGER.warning(error.getStackTrace().toString());
			error.getStackTrace();
		} // try - catch
		
		LOGGER.info("Exit program");
		
		return;	
	} // main

} // EchoClient class
