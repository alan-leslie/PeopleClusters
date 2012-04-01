/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pagelinks;

import java.util.Comparator;

/**
 *
 * @author al
 */
public class MagnitudeComparator implements Comparator<LinkMagnitude>{
	@Override
	public int compare(LinkMagnitude o1, LinkMagnitude o2) {
		return (o1.getMagnitude()>o2.getMagnitude() ? -1 : (o1==o2 ? 0 : 1));
	}
}    
