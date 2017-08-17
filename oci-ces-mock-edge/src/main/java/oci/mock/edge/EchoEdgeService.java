/**
 * 
 */
package oci.mock.edge;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import oci.template.EdgeDiscoveryService;

/**
 * @author marc
 *
 */
public class EchoEdgeService extends EdgeDiscoveryService {

	// Edge Service wide logging
    private static final Logger LOGGER = Logger.getLogger(EchoEdgeService.class.getName());
	
	// OCI service name
	public final static String	SERVICE_NAME	= "mockService";
	public final static int		SERVICE_PORT	= 9292;
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LOGGER.info("EchoEdgeService started");

		LOGGER.info("Try to open a server socket");
		try {
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
                if(inputLine.equals("exit")) return;
            } // while
			
		} catch (Exception error) {
			LOGGER.warning(error.getMessage());
			LOGGER.warning(error.getStackTrace().toString());
			
		} // try - catch
		
		LOGGER.info("Exit program");
		return;
		
	} // main

} // class EchoEdgeService
