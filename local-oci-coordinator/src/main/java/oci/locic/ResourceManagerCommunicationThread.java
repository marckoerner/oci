package oci.locic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import oci.lib.ServiceNameResolver;

/**
 * 
 * This thread handles the communication to the resource and orchestration manager
 * 
 * @author Marc Koerner
 *
 */
public class ResourceManagerCommunicationThread extends Thread {
	
	public final static int		PORT			= ServiceNameResolver.PORT + 2; // Port 5535
	
	private ServerSocket		serverSocket	= null;
	private String 				serviceName		= new String();
	private Object				lock			= new Object();
	private boolean 			disconnect		= false;
	private boolean				ret				= false;
	
	public ResourceManagerCommunicationThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		
		try {
			
			while(!this.disconnect) {
			
				LocalOciCoordinator.LOGGER.info("Waiting for Resource and Orchestration Manager registration");
				// blocking accept is ended when client connects or LOCIC main closes the server socket => exception
				Socket resourceManager = this.serverSocket.accept();
				LocalOciCoordinator.LOGGER.info("Resource and Orchestration Manager connected");
				
				// create object streams RnOM communication
				ObjectOutputStream	oos = new ObjectOutputStream(resourceManager.getOutputStream());
				ObjectInputStream	ois = new ObjectInputStream(resourceManager.getInputStream());
								
				synchronized(this.lock) {
					while(!this.disconnect) {
						try {
							this.lock.wait();
						} catch(InterruptedException error) {
							this.serviceName	= null;
							//this.disconnect		= true;
						}
						try {
							oos.writeObject(this.serviceName);
							oos.flush();
							this.ret = ois.readBoolean();
						} catch(IOException error) {
							LocalOciCoordinator.LOGGER.info("RnOM hung up");
							break;
						}
					}
				}
								
				// close connection to client
				oos.close();
				ois.close();
				resourceManager.close();
				LocalOciCoordinator.LOGGER.info("RnOM client connection closed");
				
			}
		
		} catch(Exception error) {
			
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
			error.printStackTrace();
			
		} // try - catch
		
		LocalOciCoordinator.LOGGER.info("Thread stopped");
		
	} // run
	
	/**
	 * Sends a service request to the Resource and Orchestration Manager
	 * @param serviceName Name of the service
	 */
	public void serviceRequest(String serviceName) {
		synchronized(this.lock) {	
			this.serviceName = serviceName;
			this.lock.notifyAll();		
		}
	}
	
	/**
	 * Closes the RnOM communication thread and interrupts all it's executions
	 */
	public void disconnect() {
		this.disconnect = true;
		this.interrupt();
	}

}
