package oci.gocic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;

public class LocicResolverThread extends Thread {
    
    private DatagramSocket  udpServerSocket = null;
    private boolean         running         = true;
    private GocicConfig     config          = null;
    
    public LocicResolverThread(DatagramSocket serverSocket, GocicConfig config) {
        this.udpServerSocket    = serverSocket;
        this.config             = config;
    }
    
    @Override
    public void run() {
        
        byte[]              buffer          = null;
        DatagramPacket      packet          = null;
        LocicResolverWorker worker          = null;
        
        while(running) {
            buffer  = new byte[256];
            packet  = new DatagramPacket(buffer, buffer.length);
            try {
                udpServerSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // start new worker
            worker = new LocicResolverWorker(udpServerSocket, packet, config);
            worker.start();
            
        } // while
        
    } // run
    
} // class
