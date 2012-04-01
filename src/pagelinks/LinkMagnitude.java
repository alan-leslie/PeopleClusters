
package pagelinks;

/**
 *
 * @author al
 */

public interface LinkMagnitude extends Comparable<LinkMagnitude> {
    public double getMagnitude();
    public double getMagnitudeSqd();
    public String getLink();
    public void setMagnitude(double newMag);
}    
