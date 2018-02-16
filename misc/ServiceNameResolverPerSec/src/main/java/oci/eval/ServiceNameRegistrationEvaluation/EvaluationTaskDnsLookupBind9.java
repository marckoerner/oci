package oci.eval.ServiceNameRegistrationEvaluation;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EvaluationTaskDnsLookupBind9 extends Thread
{

	public void run() {	

		long startTime = 0;
		long stopTime = 0;

		if(EvaluationDnsLookupBind9PerSec.errors > 5) 
		{
			System.out.println("Too many getEdgeServiceIpAddress failures.");
			return;
		}

		// generate random service name within service boundaries
		Double d = Math.random() * EvaluationDnsLookupBind9PerSec.entries;
		String serviceName = "service" + d.intValue() + ".locic";

		// generate random start time to model poisson arrival of client request
		if (EvaluationDnsLookupBind9PerSec.poisson)
		{
			Double randomStartTime_ms = Math.random() * (EvaluationDnsLookupBind9PerSec.periodicDelay/1000000);

			try {
				Thread.sleep(randomStartTime_ms.intValue());
			} catch (InterruptedException e) {
				// nothing
			}
		}

		try {
			startTime	= System.currentTimeMillis();
			InetAddress.getByName(serviceName);
			stopTime	= System.currentTimeMillis();
			//	    		System.out.print(address.toString() + "|");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// elimination of transient phase by ignoring the first lookups in the stats
		if (EvaluationDnsLookupBind9PerSec.numberOfLookup > (2*EvaluationDnsLookupBind9PerSec.entries))
		{
			EvaluationDnsLookupBind9PerSec.times.add((stopTime-startTime));
		}
		else
		{
			EvaluationDnsLookupBind9PerSec.numberOfLookup++;
		}
	}
}
