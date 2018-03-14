package oci.eval.ServiceNameRegistrationEvaluation;

import java.net.InetAddress;
import oci.lib.ServiceNameResolver;

public class EvaluationTask extends Thread
{

	public void run() {		
		if(EvaluationServiceNameResolverPerSec.errors > 5) 
		{
			System.out.println("Too many getEdgeServiceIpAddress failures.");
			return;
		}			

		// generate random service name within service boundaries
		Double d = Math.random() * EvaluationServiceNameResolverPerSec.entries;
		String serviceName = "service" + d.intValue();

		// generate random start time to model poission arrival of client request
		if (EvaluationServiceNameResolverPerSec.poisson)
		{
			//        	Double randomStartTime_ms = Math.random()*(EvaluationServiceNameResolverPerSec.periodicDelay);      	

			//  Exponential distributed inter arrival time calculation: x = log(1-u)/(−λ)        	
			double ln = Math.log(1-Math.random());
			double ln2 = (double) 1/EvaluationServiceNameResolverPerSec.periodicDelay;

			Double randomExponentialStartTime_ms = -ln/ln2;        	        	
			//        	System.out.println(randomExponentialStartTime_ms.intValue());

			try {
				//Thread.sleep(randomStartTime_ms.intValue());
				Thread.sleep(randomExponentialStartTime_ms.intValue());
			} catch (InterruptedException e) {
				// nothing
			}
		}

		long startTime	= System.nanoTime();
		InetAddress ipAddress = ServiceNameResolver.getEdgeServiceIpAddress(serviceName);
		long stopTime	= System.nanoTime();

		if (ipAddress != null)
		{
			// elimination of transient phase by ignoring the first lookups in the stats
//			if (EvaluationServiceNameResolverPerSec.numberOfLookup > (2*EvaluationServiceNameResolverPerSec.entries))
				EvaluationServiceNameResolverPerSec.times.add(stopTime - startTime);
//			else
//				EvaluationServiceNameResolverPerSec.numberOfLookup++;	
		}
		else
			EvaluationServiceNameResolverPerSec.nullIpCounter++;		
	}
}
