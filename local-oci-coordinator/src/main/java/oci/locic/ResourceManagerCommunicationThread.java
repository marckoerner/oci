package oci.locic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import oci.lib.ServiceNameResolver;

public class ResourceManagerCommunicationThread extends Thread {
	
	public final static int		PORT			= ServiceNameResolver.PORT + 2; // Port 5535
	
	private ServerSocket		serverSocket	= null;
	private ObjectOutputStream	oos				= null;
	private ObjectInputStream	ois				= null;
	
	private String 				serviceName		= null;
	private boolean 			disconnect		= false;
	private boolean				ret				= false;
	
	public ResourceManagerCommunicationThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		
		try {
			
			while(true) {
				
				LocalOciCoordinator.LOGGER.info("Waiting for Resource and Orchestration Manager registration");
				// blocking accept is ended when client connects or LOCIC main closes the server socket => exception
				Socket resourceManager = this.serverSocket.accept();
				LocalOciCoordinator.LOGGER.info("Resource and Orchestration Manager connected");
				
				// create object streams RnOM communication
				// TODO revise object stream approach
				this.oos = new ObjectOutputStream(resourceManager.getOutputStream());
				this.ois = new ObjectInputStream(resourceManager.getInputStream());
				
				synchronized(serviceName) {
					while(!this.disconnect) {
						try {
							serviceName.wait();
						} catch(InterruptedException error) {
							this.serviceName = null;
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
				
			} // while
		
		} catch(Exception error) {
			
			LocalOciCoordinator.LOGGER.warning(error.getMessage());
			
		} // try - catch
		
	} // run
	
	/**
	 * Sends a service request to the Resource and Orchestration Manager
	 * @param serviceName Name of the service
	 */
	public void serviceRequest(String serviceName) {
		synchronized(this.serviceName) {
			this.serviceName = serviceName;
			serviceName.notifyAll();
		}
	}
	
	/**
	 * Interrupts this thread, closes the connection to the Resource and Orchestration Manager, and eventually terminates the thread.
	 */
	public void disconnect() {
		this.disconnect = true;
		this.interrupt();
	}

}
