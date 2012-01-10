
package peopleclustrs.utils;

import java.util.*;

public class InverseDocFreqEstimatorImpl implements InverseDocFreqEstimator {

    private Map<String,Integer> tagFreq = null;
    private int totalNumDocs;
    private int highestPercentage = 100;
    private int lowestPercentage = 0;
    
    public InverseDocFreqEstimatorImpl(int totalNumDocs) {
        this.totalNumDocs = totalNumDocs;
        this.tagFreq = new HashMap<String,Integer>();
    }
    
    /**
     * 
     * @param link 
     * @return
     */
    @Override
    public double estimateInverseDocFreq(String link) {
        if(tagFreq.containsKey(link)){
            // return 0 if it has been pruned
            Integer freq = this.tagFreq.get(link);
            if ((freq == null) || (freq.intValue() == 0)){
                return 1.;
            }

            double thePercentage = freq.doubleValue()/totalNumDocs * (double)100;

            if(thePercentage < lowestPercentage){
                return 0;
            }

            if(thePercentage > highestPercentage){
                return 0; 
            }

            return Math.log(totalNumDocs/freq.doubleValue());
        }
        
        return 0.0;
    }
    
    /**
     * 
     * @param link 
     */
    @Override
    public void addCount(String link) {
        Integer count = this.tagFreq.get(link);
        if (count == null) {
            count = new Integer(1);
        } else {
            count = new Integer(count.intValue() + 1);
        }
        this.tagFreq.put(link, count);
    }

    @Override
    public int noOfLinks() {
        return tagFreq.size();
    }

    @Override
    public void outputFrequencies() {
        for (Map.Entry<String, Integer> entry : tagFreq.entrySet()) {
            Integer percentage = entry.getValue() * 100/totalNumDocs;
            System.out.println(entry.getKey() + " = " + percentage.toString() + "%");
        }
    }

    @Override
    public void prune(int lowestPercentage, int highestPercentage) {
        this.lowestPercentage = lowestPercentage;
        this.highestPercentage = highestPercentage;
    }

    @Override
    public boolean hasValid(String theLink) {
        if(tagFreq.containsKey(theLink)){
            Integer theFreq = tagFreq.get(theLink);

            int thePercentage = theFreq * 100/totalNumDocs;

            if(thePercentage >= lowestPercentage && 
                    thePercentage <= highestPercentage){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void outputInverseFrequencies() {
        for (Map.Entry<String, Integer> entry : tagFreq.entrySet()) {
            System.out.println("Inverse frequency of:" + entry.getKey() + " = " + Double.toString(estimateInverseDocFreq((String)entry.getKey())));
        }
    }
}

