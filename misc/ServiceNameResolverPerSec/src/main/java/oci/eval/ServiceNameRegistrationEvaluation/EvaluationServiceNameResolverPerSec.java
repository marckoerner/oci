package oci.eval.ServiceNameRegistrationEvaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;

import oci.lib.ServiceNameEntry;
import oci.lib.ServiceNameRegistration;

import java.util.concurrent.*;

/**
 * This application is a simple benchmark tool for the OCI name service 
 *
 */

public class EvaluationServiceNameResolverPerSec
{

	public static Vector<Long> times = new Vector<Long>();
	public static int loadCurr = 0;
	public static int errors = 0;
	public static int entries = 100;
	public static int experminentationTimeSeconds = 2;
	public static long periodicDelay;

	static File samples	= new File("samples.csv");
	static FileWriter fWriter = null;
	// value separator in csv file
	static String seperator	= ";";

	// writes probes to file
	public static void writeMeasurementResultsToFile() {

		long sum = 0;
		double average = 0;
		double avgTime_ms = 0;

		//		try
		//		{
		//			fWriter = new FileWriter(samples);
		//		}
		//		catch (IOException e)
		//		{
		//			e.printStackTrace();
		//			return;
		//		}

		//		BufferedWriter	bWriter	= new BufferedWriter(fWriter);



		for(int i=0; i<=times.size()-1; i++) {
			sum += times.get(i);
		}

		average=sum/times.size();
		avgTime_ms = average / 1000000;

		System.out.println(loadCurr + " | " + entries + " | " + errors + " | "+ String.format("%.3f", avgTime_ms));

		//		try {		
		//			bWriter.write(String.format("%.3f",avgTime_ms) + seperator);
		//			//System.out.print(String.format("%.3f",loadCurr) + seperator + String.format("%.3f", avgTime_ms) + seperator);
		//			
		//
		//			bWriter.newLine();
		//			bWriter.flush();
		//
		//			bWriter.close();
		//			fWriter.close();
		//
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//			return;
		//		}			

	} 

	public static void main( String[] args ) throws Exception
	{
		System.out.println( "LOCIC based Name Service Lookup Benchmark" );

		String	locic_ip		= "localhost";

		// delay in ms
		//int		reg_delay		= 0;

		String	serviceName	= null;
		int		serviceKey	= ServiceNameEntry.NO_KEY;

		int loadStart = 10; // number of loadCurr client requests per seconds
		int loadStep = 10;
		int loadEnd = 100;	
		//int loadCurr = 1;

		//for(loadCurr = loadStart; loadCurr >= loadEnd; loadCurr = loadCurr - loadStep)
		for(loadCurr = loadStart; loadCurr <= loadEnd; loadCurr = loadCurr + loadStep)
		{
			// 1. pre work: feed LOCIC with edge service name entries

			// start LOCIC
			String command = "java -jar /home/runge/oci-test/local-oci-coordinator.jar";
			final Process locicProcess = Runtime.getRuntime().exec(command);		

			Thread.sleep(5000);
			
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

			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

			EvaluationTask evaluationTask1 = new EvaluationTask();
			EvaluationTask evaluationTask2 = new EvaluationTask();
			EvaluationTask evaluationTask3 = new EvaluationTask();
			EvaluationTask evaluationTask4 = new EvaluationTask();

			periodicDelay = (long) 4*(1000000000/loadCurr)/2; // divided by to model poisson arrival of client requests
			long initialDelay = periodicDelay/4;

			final Future<?> f1 = scheduler.scheduleAtFixedRate(evaluationTask1, 0, periodicDelay, TimeUnit.NANOSECONDS);
			final Future<?> f2 = scheduler.scheduleAtFixedRate(evaluationTask2, initialDelay, periodicDelay, TimeUnit.NANOSECONDS);
			final Future<?> f3 = scheduler.scheduleAtFixedRate(evaluationTask3, initialDelay*2, periodicDelay, TimeUnit.NANOSECONDS);
			final Future<?> f4 = scheduler.scheduleAtFixedRate(evaluationTask4, initialDelay*3, periodicDelay, TimeUnit.NANOSECONDS);

			Runnable cancelTask = new Runnable() {
				public void run() {
					f1.cancel(true);
					f2.cancel(true);
					f3.cancel(true);
					f4.cancel(true);
					
					// kill LOCIC
					try {
						Thread.sleep(5000);
						locicProcess.destroy();
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
					EvaluationServiceNameResolverPerSec.writeMeasurementResultsToFile();		
				}
			};

			final Future<?> cancelHandle = scheduler.schedule(cancelTask, experminentationTimeSeconds, TimeUnit.SECONDS);

			while (!cancelHandle.isDone()) {
				try {
					Thread.sleep(experminentationTimeSeconds*1000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}			
			}

		}	// for loadCurr
		
		System.exit(0);

	} // main

} // App
