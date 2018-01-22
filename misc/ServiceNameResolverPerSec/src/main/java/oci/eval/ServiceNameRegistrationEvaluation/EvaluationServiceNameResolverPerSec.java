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

public class EvaluationServiceNameResolverPerSec
{

	public static Vector<Long>	times	= new Vector<Long>();
	public static int		errors		= 0;
	public static int		entries			= 10;

	public static void main( String[] args )
	{
		System.out.println( "LOCIC based Name Service Lookup Benchmark" );

		String	locic_ip		= "localhost";

		// delay in ms
		//int		reg_delay		= 0;

		String	serviceName	= null;
		int		serviceKey	= ServiceNameEntry.NO_KEY;

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

//		int loadStart = 1; // number of loadCurr client requests per seconds
//		int loadStep = 1;
//		int loadEnd = 2;	
		int loadCurr = 1;
//		
//		for(int loadCurr = loadStart; loadCurr < loadEnd; loadCurr = loadCurr + loadStep) {

			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

			EvaluationTask evaluationTask = new EvaluationTask();
			long periodicDelay = (long)(1000000000/loadCurr);
			long initialDelay = periodicDelay;

			final Future<?> f1 = scheduler.scheduleAtFixedRate(evaluationTask, initialDelay, periodicDelay,TimeUnit.NANOSECONDS);

			Runnable cancelTask = new Runnable() {
				public void run() {
					f1.cancel(true);
					EvaluationServiceNameResolverPerSec.doAfterwork();
				}
			};

			scheduler.schedule(cancelTask, 10, TimeUnit.SECONDS);

		}

//		System.exit(0);

//	}

	// 3. after work: writes probes to file

	public static void doAfterwork() {
		float time_ms = 0;
		File			samples	= new File("samples.csv");
		FileWriter		fWriter = null;
		// value separator in csv file
		String	seperator		= ";";


		try {
			fWriter = new FileWriter(samples);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		BufferedWriter	bWriter	= new BufferedWriter(fWriter);

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

	} // main
} // App
