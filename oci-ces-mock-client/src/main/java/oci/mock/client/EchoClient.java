/**
 * 
 */
package oci.mock.client;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

import oci.lib.ServiceNameResolver;


/**
 * The EchoClient is a tool which helps to test the OCI implementation. It is a mock client application.
 * 
 * @author Marc Koerner
 */
public class EchoClient {
	
	// Client wide logging
    private static final Logger LOGGER = Logger.getLogger(EchoClient.class.getName());
	
	// OCI service name
	public final static String	SERVICE_NAME	= "EchoEdgeService";
	public final static int		SERVICE_PORT	= 9292;
	
    private static final int	SOCKET_TIMEOUT	= 5000; // 5 seconds timeout

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		LOGGER.info("EchoClient started");
		
		// try to establish connection between client and server
		Socket clientSocket = null;
		try {
			// connect to edge service using the OCI ServiceNameResolver (OCI CESlib)
			LOGGER.info("Try to find OCI name service and resolve service IP");
			InetAddress ip = ServiceNameResolver.getEdgeServiceIpAddress(SERVICE_NAME);
			if(ip == null) {
				LOGGER.fine("No service found");
				LOGGER.info("Exit program");
				return;
			}
			
			LOGGER.fine("successful - service IP is " + ip.toString());
			LOGGER.info("Try to connect to server");
			clientSocket = new Socket(ip, SERVICE_PORT);
			LOGGER.info("connected");
			clientSocket.setSoTimeout(SOCKET_TIMEOUT);
			
			// create socket i/o accessible objects
			PrintWriter		out	= new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader	in	= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						
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
			error.getStackTrace();
		} // try - catch
		
		LOGGER.info("Exit program");
		
		return;	
	} // main

} // EchoClient class
