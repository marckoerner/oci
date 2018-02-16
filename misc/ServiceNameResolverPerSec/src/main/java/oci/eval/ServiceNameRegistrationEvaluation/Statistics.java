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
}

