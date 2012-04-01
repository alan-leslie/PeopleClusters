/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peopleclusters;

import java.util.List;
import pagelinks.LinkMagnitude;
import pagelinks.LinkMagnitudeVector;

/**
 *
 * @author al
 */
public class ClusterStats {
    private List<LinkMagnitude> averages;
    private LinkMagnitudeVector centre;
    private String name;

    public List<LinkMagnitude> getAverages() {
        return averages;
    }

    public LinkMagnitudeVector getCentre() {
        return centre;
    }

    public String getName() {
        return name;
    }
    
    ClusterStats(String theName, List<LinkMagnitude> theAverages, LinkMagnitudeVector theCentre){
        name = theName;
        averages = theAverages;
        centre = theCentre;
    }
}
