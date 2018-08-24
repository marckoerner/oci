package oci.gocic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class LocicResolverThread extends Thread {
    
    private DatagramSocket  udpServerSocket = null;
    private boolean         running         = true;
    private byte[]          buffer          = new byte[256]; // think about buffer / message size
    
    public LocicResolverThread(DatagramSocket serverSocket) {
        udpServerSocket = serverSocket;
    }
    
    @Override
    public void run() {
        
        while(running) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                udpServerSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            
        } // while
        
        
    } // run
    
} // class
