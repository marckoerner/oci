package oci.lib;

import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author      Marc Koerner <koerner@icsi.berkeley.edu>
 * @version     0.1
 */
public class CesEdgeSocket extends CesSocket {
	
	private String serviceName = new String();
	private Socket socket;
	
	
	public CesEdgeSocket(String serviceName, Socket serverAddress) throws UnknownHostException {
		CesSocket edgeSocket = new CesSocket(SocketType.EDGE);
		this.serviceName = serviceName;
		this.socket = serverAddress;
	}
	

} // class CesEdgeSocket
