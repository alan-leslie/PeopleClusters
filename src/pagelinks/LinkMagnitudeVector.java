/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pagelinks;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 *
 * @author al
 */
public interface LinkMagnitudeVector {
    public List<LinkMagnitude> getLinkMagnitudes();
    public Map<String,LinkMagnitude> getLinkMagnitudeMap() ;
    public LinkMagnitudeVector add(LinkMagnitudeVector o);
    public LinkMagnitudeVector add(Collection<LinkMagnitudeVector> tmList);
    public double dotProduct(LinkMagnitudeVector o) ;
}
