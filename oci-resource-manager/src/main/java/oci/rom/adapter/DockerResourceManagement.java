package oci.rom.adapter;

import java.io.File;
import java.net.InetAddress;

import oci.rom.GenericResourceManagementInterface;

/**
 * This is an implementation of the GenericResourceManagement for Docker as a resource manager
 * @author Marc Koerner
 */
public class DockerResourceManagement implements GenericResourceManagementInterface {

    @Override
    public boolean resourcesAvailable() {
        // TODO Auto-generated method stub
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
    public boolean resourcesAvailable(File edgeServiceApplicationPackage) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean startEdgeService(String edgeServiceName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean startEdgeService(File edgeServiceApplicationPackage) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean stopEdgeService(String edgeServiceName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public InetAddress getEdgeServiceIP(String edgeServiceName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEdgeServiceRunning() {
        // TODO Auto-generated method stub
        return false;
    }

}
