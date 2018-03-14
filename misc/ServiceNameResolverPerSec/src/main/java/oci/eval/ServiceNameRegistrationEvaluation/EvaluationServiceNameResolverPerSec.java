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
	// config parameters	
	public static int entries = 100;	
	public static int loadStart = 1; // number of loadCurr client requests per seconds
	public static int loadStep = 1;
	public static int loadEnd = 1000000;
	public static boolean poisson = true;
	public static int experminentationTimeSeconds = 30; // sec	
	//---

	public static int loadCurr = 0;
	public static long periodicDelay = 1000; // msec
	public static Integer numberOfLookup = 0;
	public static int errors = 0;
	public static Vector<Long> times = null;
	public static int nullIpCounter = 0;

	static File logFile	= null;
	static FileWriter fWriter = null;
	static BufferedWriter bWriter = null;
	// value separator
	static String seperator	= " | ";

	static ScheduledExecutorService scheduler;	

	// writes probes to file
	public static void writeMeasurementResultsToFile() {

		//		int transientOffset = 2*entries;
		//				System.out.print("loadCurr " + loadCurr + ": ");
		//		for(int i=0; i<=times.size()-1; i++) {
		//			System.out.print(times.get(i)/1000000 + "|");
		//		}		
		//		System.out.println();

		Statistics stats = new Statistics(times);

		String output = loadCurr + " | " + entries + " | " + experminentationTimeSeconds + " | " + String.format("%.3f", stats.getMean()/1000000) + " | " + String.format("%.3f", stats.getMedian()/1000000) + " | " + String.format("%.3f", stats.getPercentile(0.75)/1000000) + " | "+ String.format("%.3f", stats.getPercentile(0.90)/1000000) + " | " + String.format("%.3f", stats.getPercentile(0.95)/1000000) + " | " + String.format("%.3f", stats.getPercentile(0.99)/1000000) + " | "+ String.format("%.3f", stats.getStdDev()/1000000) + " | " + times.size() + " | " + nullIpCounter;

		System.out.println(output);

		try {		
			bWriter.write(output);
			bWriter.newLine();
			bWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static void main( String[] args ) throws Exception
	{			
		System.out.println( "LOCIC based Name Service Lookup Benchmark" );

		String	locic_ip		= "localhost";
		String	serviceName	= null;
		int		serviceKey	= ServiceNameEntry.NO_KEY;

		logFile	= new File(args[0]);		
		entries = Integer.parseInt(args[1]);
		loadStart = Integer.parseInt(args[2]);
		loadStep = Integer.parseInt(args[3]);

		try
		{
			fWriter = new FileWriter(logFile);
			bWriter	= new BufferedWriter(fWriter);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		// 1. pre work: feed LOCIC with edge service name entries

		for(int i = 0; i < entries; i++) {

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

		// 2. read each LOCIC edge service for elimination of transient phase by ignoring the first lookups in the stats

		for (int j =0 ; j < 4 ; j++) {
			for(int i = 0; i < entries; i++) {

				serviceName = "service" + i;

				InetAddress ipAddress = ServiceNameResolver.getEdgeServiceIpAddress(serviceName);

				if (ipAddress == null)
				{
					errors++;	
				}
			}	
		}
		
		String tableHeader = "load | entries | expT | mean | median | 75p | 90p | 95p | 99p | stdev | number | errors";
		
		System.out.println("Number of Initial Lookup Errors:" + errors );

		try {		
			bWriter.write("Number of Initial Lookup Errors:" + errors);
			bWriter.newLine();
			bWriter.write(tableHeader);
			bWriter.newLine();
			bWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}		

		System.out.println(tableHeader);

		for(loadCurr = loadStart; loadCurr <= loadEnd; loadCurr = loadCurr + loadStep)
		{
			Vector<EvaluationTask> evaluationTaskVector = new Vector<EvaluationTask>();
			final Vector<Future<?>> scheduledTaskVector = new Vector<Future<?>>();
			times = new Vector<Long>();
			numberOfLookup = 0;
			nullIpCounter = 0;

			// read each LOCIC edge service for elimination of transient phase by ignoring the first lookups in the stats			
			for (int j =0 ; j < 4 ; j++) {
				for(int i = 0; i < entries; i++) {
					serviceName = "service" + i;
					ServiceNameResolver.getEdgeServiceIpAddress(serviceName);
				}
			}

			// Adaptive load steps
			if (loadCurr < 10)
				loadStep = 1;
			else if (loadCurr < 100)
				loadStep = 10;
			else if (loadCurr < 1000)
				loadStep = 100; 
			else
				loadStep = 1000;

//			experminentationTimeSeconds = 4*entries/loadCurr;

//			if (experminentationTimeSeconds > 900)
				experminentationTimeSeconds = 900;

//			if (experminentationTimeSeconds < 30)
//				experminentationTimeSeconds = 30;

			//			experminentationTimeSeconds = 10;


			// start LOCIC
			//			String command = "java -jar /home/runge/oci-test/local-oci-coordinator.jar";

			//			final Process locicProcess = Runtime.getRuntime().exec(command);	

			//			Thread.sleep(experminentationTimeSeconds*2000);



			// 2. actual work: benchmarking edge service name resolution to obtain edge service IP 

			//			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
			//ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

			//			if (!scheduler.isShutdown()) {
			//				scheduler.shutdownNow();
			//			}

			scheduler = Executors.newScheduledThreadPool(loadCurr);			

			for (int i=0;i<loadCurr;i++) {
				EvaluationTask evaluationTask1 = new EvaluationTask();	
				evaluationTaskVector.add(evaluationTask1);				
			}

			//			EvaluationTask evaluationTask2 = new EvaluationTask();
			//			EvaluationTask evaluationTask3 = new EvaluationTask();
			//			EvaluationTask evaluationTask4 = new EvaluationTask();

			//periodicDelay = (long) 4*(1000000000/loadCurr); // multiplied by 4 for four threads
			//			periodicDelay = (long) 4*(1000000000/loadCurr);
			//			periodicDelay = (long) (1000000000/loadCurr);

			long initialDelay = 0;

			Double initialDelayStepDouble = (double) periodicDelay/loadCurr;
			long initialDelayStep = (long) initialDelayStepDouble.intValue();

			for (int i=0;i<loadCurr;i++) {	
				EvaluationTask evaluationTask1 = evaluationTaskVector.get(i);
				final Future<?> f1 = scheduler.scheduleAtFixedRate(evaluationTask1, initialDelay, periodicDelay, TimeUnit.MILLISECONDS);
				scheduledTaskVector.add(f1);
				initialDelay = initialDelay + initialDelayStep;
			}			

			//			final Future<?> f2 = scheduler.scheduleAtFixedRate(evaluationTask2, initialDelay, periodicDelay, TimeUnit.NANOSECONDS);
			//			final Future<?> f3 = scheduler.scheduleAtFixedRate(evaluationTask3, initialDelay*2, periodicDelay, TimeUnit.NANOSECONDS);
			//			final Future<?> f4 = scheduler.scheduleAtFixedRate(evaluationTask4, initialDelay*3, periodicDelay, TimeUnit.NANOSECONDS);

			Runnable cancelTask = new Runnable() {

				public void run() {

					for (int i=0;i<loadCurr;i++) {
						final Future<?> scheduledTask = scheduledTaskVector.get(i);
						scheduledTask.cancel(true);						

					}

					//					f1.cancel(true);
					//					f2.cancel(true);
					//					f3.cancel(true);
					//					f4.cancel(true);


				}
			};

			final Future<?> cancelHandle = scheduler.schedule(cancelTask, experminentationTimeSeconds, TimeUnit.SECONDS);

			while (!cancelHandle.isDone() ) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
			}

			scheduler.shutdownNow();

			while (!scheduler.isTerminated()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}								
			}

			EvaluationServiceNameResolverPerSec.writeMeasurementResultsToFile();


			// kill LOCIC


			//			try {
			//				Thread.sleep(experminentationTimeSeconds*5000);
			//				locicProcess.destroy();
			//				Thread.sleep(experminentationTimeSeconds*2000);
			//			} catch (InterruptedException e) {
			//				e.printStackTrace();
			//			}

		}	// for loadCurr

		bWriter.close();
		fWriter.close();
		System.exit(0);

	} // main

} // App
