
package peopleclustrs.utils;

/**
 *
 * @author al
 */
public interface InverseDocFreqEstimator {
    public double estimateInverseDocFreq(String link);
    public void addCount(String link);
    public int noOfLinks();
    public void outputFrequencies();
    public void prune(int lowest, int hightest);

    public boolean hasValid(String theLink);

    public void outputInverseFrequencies();
}
