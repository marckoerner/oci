package oci.eval.ServiceNameRegistrationEvaluation;

import java.util.Collections;
import java.util.Vector;

public class Statistics {
    Vector<Long> data;
    int size;   

    public Statistics(Vector<Long> data) {
        this.data = data;
        size = data.size();
    }   

    double getMean() {
        long sum = 0;
        for(Long a : data)
            sum += a;
        return (double) (sum/size);
    }

    double getVariance() {
        double mean = getMean();
        double temp = 0;
        for(long a :data)
            temp += (a-mean)*(a-mean);
        return temp/(size-1);
    }

    double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double getMedian() {
       Collections.sort(data);

       if (data.size() % 2 == 0) {
          return (data.get((data.size() / 2) - 1) + data.get(data.size() / 2)) / 2.0;
       } 
       return data.get(data.size() / 2);
    }
    
    public double getPercentile(double percentile) {
    	
    	if (percentile <=0 || percentile >=1) {
    		return -1;
    	}
    	
    	Collections.sort(data);
    	
    	Double p = data.size()*percentile;    	
    	int k = p.intValue();
    	
    	if ((k+1) >= data.size())
    	{
    		return data.get(data.size()-1);
    	}
    	else
    	{
    		return 0.5*(data.get(k)+data.get(k+1));
    	}
    	
    	    	
    }
}

