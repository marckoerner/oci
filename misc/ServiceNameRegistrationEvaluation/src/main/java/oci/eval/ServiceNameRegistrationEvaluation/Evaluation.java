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
        
        int		entries			= 200;
        int		probes			= 20;
        int		min_delay		= 0;
        int		max_delay		= 0;
        
        // if no arguments are given
        if(args.length != 4) {
        	System.out.println("Usage Parameters: [entries] [probes] [min_delay_ms] [max_delay_ms]");
        	System.exit(0);
        } else {
        	entries		= Integer.parseInt(args[1]);
        	probes		= Integer.parseInt(args[1]);
        	min_delay	= Integer.parseInt(args[2]);
        	max_delay	= Integer.parseInt(args[3]);
        }

        String	locic_ip		= "localhost";
        
        int		probe_offset	= 0;
        
        // value separator in csv file
        String	fileName		= "samples_read_" + entries + "_" + probes + "_rand-" + min_delay + "-" + max_delay + ".csv";
        String	seperator		= ";";
        
        long	startTime;
        long	stopTime;
        
        String	serviceName	= null;
        int		serviceKey	= ServiceNameEntry.NO_KEY;
        int		errors		= 0;
        
        File			samples	= new File(fileName);
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
    			// Thread.sleep(pause());

    		} catch(Exception error) {
    			error.printStackTrace();
    			errors++;
    		}
    		
        }
        
        // write probes to file
        float time_ms = 0;
        float time_ms_average = 0;
    	try {
    		for(Long time : times) {
    			time_ms = (float) time / (float) 1000000;
    			time_ms_average += time_ms;
				bWriter.write(String.format("%.3f", time_ms) + seperator);
				System.out.print(String.format("%.3f", time_ms) + seperator);
    		}
    		bWriter.newLine();
    		bWriter.flush();
    		
    		time_ms_average = time_ms_average - (float) (times.get(0) / 1000000);
    		time_ms_average = time_ms_average / (entries - 1);
    		bWriter.write(String.format("%.3f",time_ms_average) + seperator);
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
        	
        	// generate random service name within service boundaries
        	Double d = Math.random() * entries;
        	serviceName = "test" + d.intValue();
            //serviceName = "test" + probe;
        	
    		try {
    			startTime	= System.nanoTime();
    			edge_service_ip	= ServiceNameResolver.getEdgeServiceIpAddress(serviceName);
    			stopTime	= System.nanoTime();
    			times.add(stopTime - startTime);
    			if(serviceKey == ServiceNameEntry.NO_KEY) {
    				errors++;
    			}
    			Thread.sleep(pause(min_delay, max_delay)); // ms

    		} catch(Exception error) {
    			error.printStackTrace();
    			errors++;
    		}
    		
        }
        
        // writes probes to file
        time_ms_average = 0;
    	try {
    		for(Long time : times) {
    			time_ms = (float) time / (float) 1000000;
    			time_ms_average += time_ms;
				bWriter.write(String.format("%.3f",time_ms) + seperator);
				System.out.print(String.format("%.3f", time_ms) + seperator);
    		}
    		bWriter.newLine();
    		bWriter.flush();
    		
    		time_ms_average = time_ms_average - (float) (times.get(0) / 1000000);
    		time_ms_average = time_ms_average / (probes - 1);
    		bWriter.write(String.format("%.3f",time_ms_average) + seperator);
    		bWriter.newLine();
    		bWriter.flush();
    		System.out.println();
    		
    		bWriter.close();
    		fWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
    	
    	// measure just system compute performance
    	startTime	= System.nanoTime();
    	double rand_sum = Math.random() + Math.random();
		stopTime	= System.nanoTime();
		System.out.println("Performance1: " + (stopTime - startTime));
		
		startTime	= System.nanoTime();
    	rand_sum	= Math.random() + Math.random();
		stopTime	= System.nanoTime();
		System.out.println("Performance2: " + (stopTime - startTime));
		
		startTime	= System.nanoTime();
    	rand_sum	= Math.random() + Math.random();
		stopTime	= System.nanoTime();
		System.out.println("Performance3: " + (stopTime - startTime));
    	
		startTime	= System.nanoTime();
    	rand_sum	= 6+10;
		stopTime	= System.nanoTime();
		System.out.println("Performance4: " + (stopTime - startTime));
		
		startTime	= System.nanoTime();
    	rand_sum	= 36+10;
		stopTime	= System.nanoTime();
		System.out.println("Performance5: " + (stopTime - startTime));
    	
    	

        return;
    } // main
    
    /**
     * provides a random value between 1000 and 2500
     * @return random value
     */
    public static long pause() {
    	long offset = 1000; //ms
    	long max	= 2500;
    	long pause = 0;
    	pause = (long) (Math.random() * max) + offset; // time between [offset] and [offset + max]	
    	return pause;
    }

    /**
     * provides a random value between min and max
     * @param min smallest random value
     * @param max biggest random value
     * @return random value
     */
    public static long pause(int min, int max) {
    	long pause = 0;
    	pause = (long) (Math.random() * (max-min)) + min; // time between [offset] and [offset + max]
    	return pause;
    }
    
    
} // App
