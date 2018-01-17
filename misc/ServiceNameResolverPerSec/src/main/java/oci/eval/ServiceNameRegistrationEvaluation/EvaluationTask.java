package oci.eval.ServiceNameRegistrationEvaluation;

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
		
		try {
			long startTime	= System.nanoTime();
			ServiceNameResolver.getEdgeServiceIpAddress(serviceName);
			long stopTime	= System.nanoTime();
			EvaluationServiceNameResolverPerSec.times.add(stopTime - startTime);
			
		} 
		catch(Exception error) {
			error.printStackTrace();
			EvaluationServiceNameResolverPerSec.errors++;
		}
	}
}
