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

import java.util.concurrent.*;

/**
 * This application is a simple benchmark tool for the OCI name service 
 *
 */
public class Evaluation implements Runnable
{
	
	static Vector<Long>	times	= new Vector<Long>();
	static int		errors		= 0;
	static int		entries			= 10;
	
	
	public void run() {
		
		if(errors > 5) 
		{
			System.out.println("Too many getEdgeServiceIpAddress failures.");
			return;
		}
			
		
    	// generate random service name within service boundaries
    	Double d = Math.random() * entries;
    	String serviceName = "service" + d.intValue();
		
		try {
			long startTime	= System.nanoTime();
			ServiceNameResolver.getEdgeServiceIpAddress(serviceName);
			long stopTime	= System.nanoTime();
			times.add(stopTime - startTime);
		} catch(Exception error) {
			error.printStackTrace();
			errors++;
		}
	}

	public static void main( String[] args )
	{
		System.out.println( "LOCIC based Name Service Lookup Benchmark" );

		String	locic_ip		= "localhost";

		// delay in ms
		//int		reg_delay		= 0;
		// value separator in csv file
		String	seperator		= ";";

		String	serviceName	= null;
		int		serviceKey	= ServiceNameEntry.NO_KEY;


		File			samples	= new File("samples.csv");
		FileWriter		fWriter = null;
		try {
			fWriter = new FileWriter(samples);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		BufferedWriter	bWriter	= new BufferedWriter(fWriter);


		// 1. pre work: feed LOCIC with edge service name entries
		
		for(int i = 0; i < entries; i++) {

			//TODO: what is that?
			if(errors > 5) return;

			serviceName = "service" + i;

			serviceKey = ServiceNameEntry.NO_KEY;
			try {
				serviceKey	= ServiceNameRegistration.registerEdgeService(serviceName, InetAddress.getByName(locic_ip));
				if(serviceKey == ServiceNameEntry.NO_KEY) {
					errors++;
				}


			} catch(Exception error) {
				error.printStackTrace();
				errors++;
			}

		}


		// 2. actual work: benchmarking edge service name resolution to obtain edge service IP 
		
//		float loadStart = 1;
//		float loadStep = 1;
//		float loadEnd = 1;
//
//		for(float loadCurr = loadStart; loadCurr < loadEnd; loadCurr = loadCurr + loadStep) {


			ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
			
			int loadCurr = 1000; // 1000 client requests per seconds

			Runnable task = new Evaluation();
			long initialDelay = 500000000;
			long periodicDelay = (long)(1000000000/loadCurr);

			final Future<?> f1 = scheduler.scheduleAtFixedRate(task, initialDelay, periodicDelay,TimeUnit.NANOSECONDS);
			
			
		    Runnable cancelTask = new Runnable() {
		        public void run() {
		        	f1.cancel(true);
		        }
		    };
			
			scheduler.schedule(cancelTask, 5, TimeUnit.SECONDS);
			

//		}

		// 3. after work: writes probes to file

		float time_ms = 0;
		
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
