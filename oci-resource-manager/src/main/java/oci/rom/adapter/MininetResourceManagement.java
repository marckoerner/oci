/**
 * 
 */
package oci.rom.adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import oci.rom.GenericResourceManagementInterface;
import oci.rom.ResourceAndOrchestrationManager;

/**
 * This is an implementation of the GenericResourceManagement for the Mininet OCI-RnOM
 * @author Marc Koerner
 */
public class MininetResourceManagement implements GenericResourceManagementInterface {
	
	//public	final static String			HOSTNAME		= "c10.millennium.berkeley.edu";
	public	final static String			HOSTNAME		= "localhost";
	//public	final static byte[]			IP				= {(byte) 127, (byte) 0, (byte) 0, (byte) 1}; 	// LOCIC IP
    public	final static int			PORT			= 8383;
    
	private Socket 			socket		= null;
	private PrintWriter		out			= null;
	//private BufferedWriter	out 	= null;
	private BufferedReader	in			= null;
	
	public MininetResourceManagement() {
		try {
			this.socket = new Socket(InetAddress.getByName(HOSTNAME), PORT);
			this.out	= new PrintWriter(this.socket.getOutputStream(), true);
			//this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			this.in		= new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		} catch(Exception error) {
			ResourceAndOrchestrationManager.LOGGER.warning("error establishing connection to mininet RnOM");
		}
	}
	
	public MininetResourceManagement(InetAddress address) {
		try {
			this.socket = new Socket(address, PORT);
			this.out	= new PrintWriter(this.socket.getOutputStream(), true);
			//this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			this.in		= new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		} catch(Exception error) {
			ResourceAndOrchestrationManager.LOGGER.warning("error establishing connection to mininet RnOM");
		}
	}

	public boolean resourcesAvailable() {
		// TODO: implement resources available request
		return true;
	}

	public boolean startEdgeService(String edgeServiceName) {
		boolean ret = false;
		try {
			this.out.print("start " + edgeServiceName);
			this.out.flush();
			String reply = this.in.readLine();
			if(reply.equals("service " + edgeServiceName + " started")) ret = true;
		} catch(Exception error) {
			ResourceAndOrchestrationManager.LOGGER.warning("error while trying to start edge service with mininet RnOM");
			ret = false;
		}
		return ret;
	}

	public boolean stopEdgeService(String edgeServiceName) {
		boolean ret = false;
		try {
			this.out.print("stop " + edgeServiceName);
			this.out.flush();
			String reply = this.in.readLine();
			if(reply.equals("service " + edgeServiceName + " stopped")) ret = true;
		} catch(Exception error) {
			ResourceAndOrchestrationManager.LOGGER.warning("error while trying to stop edge service with mininet RnOM");
			ret = false;
		}
		return ret;
	}

	public InetAddress getEdgeServiceIP(String edgeServiceName) {
		InetAddress ret = null;
		try {
			this.out.print("getAddress " + edgeServiceName);
			this.out.flush();
			String reply = this.in.readLine();
			
			String[] words = reply.split("\\s+");
			if(edgeServiceName.equals(words[0]) && words.length == 5) {
				String ip = words[4];
				ret = InetAddress.getByName(ip);
			}			
		} catch(Exception error) {
			ResourceAndOrchestrationManager.LOGGER.warning("error while trying to get edge service IP address with mininet RnOM");
			ret = null;
		}
		return ret;
	}

	public boolean isEdgeServiceRunning() {
		//TODO implement is running check / locally?
		return false;
	}

	@Override
	public boolean resourcesAvailable(int cpu, int memory, int disk) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resourcesAvailable(int cpu_cores, int frequency) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startEdgeService(File edgeServiceApplicationPackage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resourcesAvailable(File edgeServiceApplicationPackage) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
