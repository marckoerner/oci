/**
 * 
 */
package oci.lib;

/**
 * @author Marc Koerner
 * This class contains all OCI framework constants
 */
public final class GlobalConstants {
	
	public final static byte[]			LOCALHOST_BYTE				= {(byte) 127, (byte) 0, (byte) 0, (byte) 1}; 	// LOCIC IP (first iteration)
	public final static String			LOCALHOST_STRING			= "localhost";
	public final static String			LOCALHOST_STRING_BYTE		= "127.0.0.1";
	public final static String			C10_DNS_STRING				= "c10.millennium.berkeley.edu";
	public final static byte[]			MININET_VM1_BYTE			= {(byte) 192, (byte) 168, (byte) 0, (byte) 10};
	
	public final static byte[]			LOCIC_IP_BYTE				= LOCALHOST_BYTE;
	public final static String			LOCIC_IP_STRING				= C10_DNS_STRING;

	
	public final static int				PORT_NAME_SERVICE			= 5533; 										// OCI name service port (DNS 53)
	public final static int				PORT_NAME_REGISTRATION		= PORT_NAME_SERVICE + 1; 						// Port 5534 / Name Service Registration 
	
    public final static int				SOCKET_TIMEOUT				= 5000;											// 5 seconds timeout

} // class
