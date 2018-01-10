package oci.eval.ServiceNameRegistrationEvaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;

import oci.lib.ServiceNameEntry;
import oci.lib.ServiceNameRegistration;
import oci.lib.ServiceNameResolver;

/**
 * This application is a simple benchmark tool for the OCI name service 
 *
 */
public class Evaluation 
{
    public static void main( String[] args )
    {
        System.out.println( "LOCIC based Name Service Lookup Benchmark" );
        
        String	locic_ip		= "localhost";
        int		entries			= 100;
        int		probes			= 50;
        int		probe_offset	= 0;
        // delay in ms
        int		reg_delay		= 0;
        int		req_delay		= 0;
        // value separator in csv file
        String	seperator		= ";";
        
        long	startTime;
        long	stopTime;
        
        String	serviceName	= null;
        int		serviceKey	= ServiceNameEntry.NO_KEY;
        int		errors		= 0;
        
        File			samples	= new File("samples.csv");
        FileWriter		fWriter = null;
		try {
			fWriter = new FileWriter(samples);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        BufferedWriter	bWriter	= new BufferedWriter(fWriter);
        Vector<Long>	times	= new Vector<Long>();
        
        // feed LOCIC with edge service name entries
        for(int i = 0; i < entries; i++) {
        	
        	if(errors > 5) return;
        	
        	serviceName = "test" + i;
        	
        	serviceKey = ServiceNameEntry.NO_KEY;
    		try {
    			startTime	= System.nanoTime();
    			serviceKey	= ServiceNameRegistration.registerEdgeService(serviceName, InetAddress.getByName(locic_ip));
    			stopTime	= System.nanoTime();
    			times.add(stopTime - startTime);
    			if(serviceKey == ServiceNameEntry.NO_KEY) {
    				errors++;
    			}
    			Thread.sleep(reg_delay);

    		} catch(Exception error) {
    			error.printStackTrace();
    			errors++;
    		}
    		
        }
        
        // write probes to file
        float time_ms = 0;
    	try {
    		for(Long time : times) {
    			time_ms = (float) time / (float) 1000000;
				bWriter.write(String.format("%.3f", time_ms) + seperator);
				System.out.print(String.format("%.3f", time_ms) + seperator);
    		}
    		bWriter.newLine();
    		bWriter.flush();
    		System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        // System.out.println( "Summary: " );

        times = null;
        times = new Vector<Long>();
        
        InetAddress edge_service_ip = null;
        
        int probe = 0 + probe_offset;
    	        
        // obtain edge service IP information
        for(int i = 0; i < probes; i++) {
        	
        	if(errors > 5) return;
        	
            serviceName = "test" + probe;
        	
    		try {
    			startTime	= System.nanoTime();
    			edge_service_ip	= ServiceNameResolver.getEdgeServiceIpAddress(serviceName);
    			stopTime	= System.nanoTime();
    			times.add(stopTime - startTime);
    			if(serviceKey == ServiceNameEntry.NO_KEY) {
    				errors++;
    			}
    			Thread.sleep(req_delay); // ms

    		} catch(Exception error) {
    			error.printStackTrace();
    			errors++;
    		}
    		
        }
        
        // writes probes to file
    	try {
    		for(Long time : times) {
    			time_ms = (float) time / (float) 1000000;
				bWriter.write(String.format("%.3f",time_ms) + seperator);
				System.out.print(String.format("%.3f", time_ms) + seperator);
    		}
    		bWriter.newLine();
    		bWriter.flush();
    		System.out.println();
    		
    		bWriter.close();
    		fWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

        return;
    } // main
} // App
