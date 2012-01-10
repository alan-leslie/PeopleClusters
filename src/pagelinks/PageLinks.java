/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pagelinks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author al
 */
public class PageLinks implements WebLinkDataItem {

    final String thisLink;
    List<LinkMagnitude> otherLinks;
    private Integer clusterId;

    PageLinks(String thisLink) {
        this.thisLink = thisLink;
        otherLinks = new ArrayList<LinkMagnitude>();
    }

    public void addLink(LinkMagnitude theMag) {
        otherLinks.add(theMag);
    }

    int compareTo(PageLinks theOther) {
        int thisNumLinks = numLinks();
        int otherNumLinks = theOther.numLinks();

        if (thisNumLinks > otherNumLinks) {
            return 1;
        } else if (thisNumLinks < otherNumLinks) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int numLinks() {
        return otherLinks.size();
    }

    @Override
    public String getSource() {
        return thisLink;
    }

    @Override
    public String getLinkAt(int index) {
        if (index >= 0 && index < otherLinks.size()) {
            LinkMagnitude theMag = otherLinks.get(index);
            return theMag.getLink();
        } else {
            return "";
        }
    }

//    @Override
//    public Map<String, String> getAttributeMap() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
    @Override
    public List<String> getLinks() {
        List<String> retVal = new ArrayList<String>();

        for (int index = 0; index < numLinks(); ++index) {
            LinkMagnitude theLink = otherLinks.get(index);
            if(theLink.getMagnitude() > 0.0){
                retVal.add(theLink.getLink());
            }
        }

        return retVal;
    }

    @Override
    public LinkMagnitudeVector getLinkMagnitudeVector() {
        List<LinkMagnitude> linkMag = new ArrayList<LinkMagnitude>();
        
        for (int index = 0; index < numLinks(); ++index) {
            String theLink = getLinkAt(index);
            LinkMagnitude theMag = new LinkMagnitudeImpl(theLink, 1.0);
            linkMag.add(theMag);
        }

        LinkMagnitudeVector retVal = new LinkMagnitudeVectorImpl(linkMag);
        return retVal;
    }

    @Override
    public Integer getClusterId() {
        return clusterId;
    }

    @Override
    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }
}
