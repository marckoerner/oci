package oci.locic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import oci.lib.ServiceNameResolver;

public class ResourceManagerCommunicationThread extends Thread {
	
	public final static int		PORT			= ServiceNameResolver.PORT + 2; // Port 5535
	
	private ServerSocket		serverSocket	= null;
	private String 				serviceName		= new String();
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
				
				synchronized(serviceName) {
					while(!this.disconnect) {
						try {
							serviceName.wait();
						} catch(InterruptedException error) {
							this.serviceName	= null;
							this.disconnect		= true;
						}
						oos.writeObject(serviceName);
						oos.flush();
						this.ret = ois.readBoolean();
					}
				}
								
				// close connection to client
				oos.close();
				ois.close();
				resourceManager.close();
				LocalOciCoordinator.LOGGER.info("Resource and Orchestration Manager connection closed");
				
			}
		
		} catch(Exception error) {
			
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
			
		} // try - catch
		
		LocalOciCoordinator.LOGGER.info("Resource and Orchestration Manager connection Thread closed");
		
	} // run
	
	/**
	 * Sends a service request to the Resource and Orchestration Manager
	 * @param serviceName Name of the service
	 */
	public synchronized void serviceRequest(String serviceName) {
		//synchronized(this.serviceName) {
			this.serviceName = serviceName;
			this.serviceName.notifyAll();
		//}
	}
	
	/**
	 * Interrupts this thread, closes the connection to the Resource and Orchestration Manager, and eventually terminates the thread.
	 */
	public void disconnect() {
		this.disconnect = true;
		this.interrupt();
	}

}