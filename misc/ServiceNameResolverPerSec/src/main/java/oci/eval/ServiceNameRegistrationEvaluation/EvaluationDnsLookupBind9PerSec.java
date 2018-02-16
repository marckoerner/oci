package oci.eval.ServiceNameRegistrationEvaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This application is a simple benchmark tool for the OCI name service 
 *
 */

public class EvaluationDnsLookupBind9PerSec
{

	public static Vector<Long> times = null;
	public static int loadCurr = 0;
	public static int errors = 0;
	public static int entries = 100;
	public static Integer numberOfLookup = 0;
	public static int experminentationTimeSeconds = 60;
	public static long periodicDelay;
	public static boolean poisson = true;

	static File logFile	= null;
	static FileWriter fWriter = null;
	static BufferedWriter bWriter = null;
	// value separator
	static String seperator	= " | ";

	static ScheduledExecutorService scheduler;

	// writes probes to file
	public static void writeMeasurementResultsToFile() {

		Statistics stats = new Statistics(times);

		System.out.println(loadCurr + " | " + entries + " | " + errors + " | " + String.format("%.3f", stats.getMean()) + " | " + String.format("%.3f", stats.getMedian()) + " | " + String.format("%.3f", stats.getStdDev()) + " | " + times.size());

		try {		
			bWriter.write(loadCurr + " | " + entries + " | " + errors + " | " + String.format("%.3f", stats.getMean()) + " | " + String.format("%.3f", stats.getMedian()) + " | " + String.format("%.3f", stats.getStdDev()) + " | " + times.size());		
			bWriter.newLine();
			bWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}		
	} 

	public static void main( String[] args ) throws Exception
	{
    	//Override system DNS setting with DNS server on localhost
		System.setProperty("sun.net.spi.nameservice.provider.1","dns,dnsjava");
		System.setProperty("sun.net.spi.nameservice.nameservers", "127.0.0.1");		
		
		System.out.println( "LOCIC based Name Service Lookup Benchmark" );
		System.out.println( "load | entries | err | mean | median | stdev | number" );

		int loadStart = 100; // number of loadCurr client requests per seconds
		int loadStep = 100;
		int loadEnd = 1000000;

		logFile	= new File(args[0]);

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

		for(loadCurr = loadStart; loadCurr <= loadEnd; loadCurr = loadCurr + loadStep)
		{
			Vector<EvaluationTaskDnsLookupBind9> evaluationTaskVector = new Vector<EvaluationTaskDnsLookupBind9>();
			times = new Vector<Long>();
			final Vector<Future<?>> scheduledTaskVector = new Vector<Future<?>>();
			numberOfLookup = 0;
			
			scheduler = Executors.newScheduledThreadPool(loadCurr);			

			for (int i=0;i<loadCurr;i++) {
				EvaluationTaskDnsLookupBind9 evaluationTask1 = new EvaluationTaskDnsLookupBind9();	
				evaluationTaskVector.add(evaluationTask1);				
			}


			//periodicDelay = (long) 4*(1000000000/loadCurr)/2; // multiplied by 4 for four threads; divided by 2 to model poisson arrival of client requests
			//			periodicDelay = (long) 4*(1000000000/loadCurr);
			//			periodicDelay = (long) (1000000000/loadCurr);
			periodicDelay = 1000000000;
			//			long initialDelay = periodicDelay/4;


			for (int i=0;i<loadCurr;i++) {
				EvaluationTaskDnsLookupBind9 evaluationTask1 = evaluationTaskVector.get(i);
				final Future<?> f1 = scheduler.scheduleAtFixedRate(evaluationTask1, 0, periodicDelay, TimeUnit.NANOSECONDS);
				scheduledTaskVector.add(f1);				
			}			

			Runnable cancelTask = new Runnable() {
				
				public void run() {

					for (int i=0;i<loadCurr;i++) {
						final Future<?> scheduledTask = scheduledTaskVector.get(i);
						scheduledTask.cancel(true);						

					}
				}
			};

			final Future<?> cancelHandle = scheduler.schedule(cancelTask, experminentationTimeSeconds, TimeUnit.SECONDS);

			while (!cancelHandle.isDone() ) {
				try {
					Thread.sleep(experminentationTimeSeconds*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
			}

			scheduler.shutdownNow();			
	
			while (!scheduler.isTerminated()) {
				try {
					Thread.sleep(experminentationTimeSeconds*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}								
			}		
			
			EvaluationDnsLookupBind9PerSec.writeMeasurementResultsToFile();
			
		}	// for loadCurr

		bWriter.close();
		fWriter.close();
		System.exit(0);

	} // main

} // App
