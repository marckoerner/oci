
package oci.mock.edge;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import oci.lib.ServiceNameEntry;
import oci.lib.ServiceNameRegistration;
import oci.template.EdgeDiscoveryService;

/**
 * The EchoEdgeService is a tool which helps to test the OCI implementation. It is a mock edge application.
 * 
 * @author Marc Koerner
 */
public class EchoEdgeService extends EdgeDiscoveryService {

	// Edge Service wide logging
    private static final Logger LOGGER = Logger.getLogger(EchoEdgeService.class.getName());
	
	// OCI service name
	public final static String	SERVICE_NAME	= "Echo";
	public final static int		SERVICE_PORT	= 9292;
	
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// set logging output to console
		Handler handlerObj = new ConsoleHandler();
		handlerObj.setLevel(Level.ALL);
		LOGGER.addHandler(handlerObj);
		LOGGER.setLevel(Level.ALL);
		LOGGER.setUseParentHandlers(false);
		
		LOGGER.info("EchoEdgeService started");

		LOGGER.info("Try to open a server socket");
		int serviceKey = ServiceNameEntry.NO_KEY;
		try {
			// DEPRECATED: registration is now covered by RnOM
			/**
			serviceKey = ServiceNameRegistration.registerEdgeService(SERVICE_NAME, InetAddress.getByName("localhost"));
			if(serviceKey == ServiceNameEntry.NO_KEY) {
				LOGGER.info("Service Name Registration failed");
				LOGGER.info("Exit program");
				return;
			}
			**/
			
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(SERVICE_PORT);
			// TODO bind address implementation via service template or ServiceNameResolver e.g.:
			// ServerSocket serverSocket = new ServerSocket(ServiceNameResolver.getEdgeServiceIpAddress(SERVICE_NAME), SERVICE_PORT);
			
			LOGGER.info("Waiting for client connection");
			Socket clientSocket = serverSocket.accept();
			LOGGER.info("Client connected");
			
			// get socket i/o
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// main loop
			String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
                System.out.println(inputLine);
                if(inputLine.equals("exit")) break;
            } // while
            
            boolean serviceUnregistered = ServiceNameRegistration.unregisterEdgeService(SERVICE_NAME, serviceKey);
			LOGGER.info("unregisterEdgeService: " + serviceUnregistered);
			
		} catch (Exception error) {
			LOGGER.warning(error.getMessage());
			LOGGER.warning(error.getStackTrace().toString());
			
			if(serviceKey != ServiceNameEntry.NO_KEY) {
				try {
					ServiceNameRegistration.unregisterEdgeService(SERVICE_NAME, serviceKey);
				} catch(Exception e) {
					LOGGER.warning("Failed to de-register service key!");
				}
			}
			
		} // try - catch
		
		LOGGER.info("Exit program");
		return;
		
	} // main

} // class EchoEdgeService
