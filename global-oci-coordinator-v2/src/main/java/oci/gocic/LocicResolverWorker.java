/**
 * 
 */
package oci.gocic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;

/**
 * An instance of a this class determines to which LOCIC a specific client belongs and sends its IP address
 * @author Marc Koerner
 */
public class LocicResolverWorker extends Thread {
    
    private DatagramSocket  udpServerSocket = null;
    private GocicConfig     config          = null;
    private DatagramPacket  packet          = null;
    
    public LocicResolverWorker(DatagramSocket serverSocket, DatagramPacket packet, GocicConfig config) {
        this.udpServerSocket    = serverSocket;
        this.packet             = packet;
        this.config             = config;
    }

    @Override
    public void run() {
        
        int             client_port     = 0;
        InetAddress     ip              = null;

        ip          = packet.getAddress();
        client_port = packet.getPort();
        
        Locic               locic   = null;
        Iterator<Locic>     itr     = this.config.getLocics().iterator();
        
        // check if client belongs to a particular LOCIC's sub-network
        while(itr.hasNext()) {         
            locic = (Locic) itr.next();
            if(locic.belongsToSubnet(ip)) {
                // send LOCIC IP to client
                DatagramPacket  reply = new DatagramPacket(locic.getIpString().getBytes(),
                                                            locic.getIpString().getBytes().length,
                                                            ip, client_port);
                try {
                    udpServerSocket.send(reply);
                } catch (IOException e) {
                    GlobalOciCoordinator.LOGGER.warning(e.getMessage());
                }
                break;
            }
            
        } // while
        
        // close connection?
        
    } // run
    
} // class
