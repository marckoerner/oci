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
 * @author Marc Koerner
 *
 */
public class LocicResolverWorker extends Thread {
    
    private DatagramSocket  udpServerSocket = null;
    private boolean         running         = true;
    private byte[]          buffer          = new byte[256]; // think about buffer / message size
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
        byte[]          data            = null;

        // extract client ip and port
        ip          = packet.getAddress();
        client_port = packet.getPort();
        data        = new byte[packet.getLength()]; // check if this is necessary 
        
        // check client sub-network
        boolean             ret     = false;
        Locic               locic   = null;
        Iterator<Locic>     itr     = this.config.getLocics().iterator();
        
        while(itr.hasNext()) {         
            
            locic = (Locic) itr.next();
            if(locic.belongsToSubnet(ip)) {
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
