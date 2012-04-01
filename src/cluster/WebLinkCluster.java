package cluster;

import pagelinks.LinkMagnitudeVector;
import pagelinks.WebLinkDataItem;
import java.util.List;
import java.util.Set;
import pagelinks.LinkMagnitude;

public interface WebLinkCluster {
    public LinkMagnitudeVector getCenter();
    public void computeCenter();
    public int getClusterId() ;
    public void setClusterId(int newId); 
    public String getTitle() ;
    public List<WebLinkDataItem> getDataItems();
    public void hierCluster(Clusterer theClusterer);
    public List<WebLinkCluster> getSubClusters();
    public Set<WebLinkDataItem> getElements();
    public WebLinkCluster copy();
    public String getElementsAsString();

    public void addSubCluster(WebLinkCluster cluster);
    public String asXML();

    public void addDataItem(WebLinkDataItem item);

    public void clearItems();
    
    public List<LinkMagnitude> getAverages();
    public Set<WebLinkDataItem> getAllItems();
}
