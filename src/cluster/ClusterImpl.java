package cluster;

import java.util.ArrayList;
import java.util.Collection;
import pagelinks.LinkMagnitudeVectorImpl;
import pagelinks.LinkMagnitudeVector;
import pagelinks.WebLinkDataItem;
import pagelinks.LinkMagnitude;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import pagelinks.DataSetManager;
import pagelinks.LinkMagnitudeImpl;
import pagelinks.MagnitudeComparator;
import pagelinks.PageLinksDataSetManagerImpl;

public class ClusterImpl implements WebLinkCluster {

    public static int CLUSTER_NO = 3;
    private static int idCounter = 0;
    private LinkMagnitudeVector center = null;
    private List<WebLinkDataItem> items = null;
    private List<WebLinkCluster> subClusters = null;
    private int clusterId;

    public ClusterImpl(int clusterId) {
        this.clusterId = idCounter++;
        this.items = new ArrayList<WebLinkDataItem>();
    }

    public ClusterImpl(int clusterId,
            WebLinkDataItem theItem) {
        this.clusterId = idCounter++;
        this.items = new ArrayList<WebLinkDataItem>();
        items.add(theItem);
    }

    public ClusterImpl(int clusterId,
            DataSetManager pt) {
        this.clusterId = idCounter++;
        try {
            this.items = pt.calculateLinkMagnitudes();
        } catch (Exception ex) {
            this.items = new ArrayList<WebLinkDataItem>();
            Logger.getLogger(ClusterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ClusterImpl(int clusterId,
            List<WebLinkDataItem> theItems) {
        this.clusterId = idCounter++;
        this.items = theItems;
    }

    @Override
    public void computeCenter() {
        if (this.items.isEmpty()) {
            return;
        }
        List<LinkMagnitudeVector> tmList = new ArrayList<LinkMagnitudeVector>();
        for (WebLinkDataItem item : items) {
            tmList.add(item.getLinkMagnitudeVector());
        }
        List<LinkMagnitude> emptyList = Collections.emptyList();
        LinkMagnitudeVector empty = new LinkMagnitudeVectorImpl(emptyList);
        this.center = empty.add(tmList);
    }

    @Override
    public int getClusterId() {
        return this.clusterId;
    }

    @Override
    public void addDataItem(WebLinkDataItem item) {
        items.add(item);
    }

    @Override
    public LinkMagnitudeVector getCenter() {
        return center;
    }

    // TODO - sort the set by magnitudes
    @Override
    public List<LinkMagnitude> getAverages() {
        List<LinkMagnitude> retVal = new ArrayList<LinkMagnitude>();
        Set<WebLinkDataItem> theItems = getElements();

        for (WebLinkDataItem theItem : theItems) {
            for (String theLink : theItem.getLinks()) {
                LinkMagnitude theMag = findLink(theLink, retVal);

                if (theMag == null) {
                    LinkMagnitudeImpl theLinkAverage = new LinkMagnitudeImpl(theLink, 1.0);
                    retVal.add(theLinkAverage);
                } else {
                    theMag.setMagnitude(theMag.getMagnitude() + 1.0);
                }
            }
        }

        for (LinkMagnitude theMagnitude : retVal) {
            double dMag = theMagnitude.getMagnitude();
            theMagnitude.setMagnitude(dMag / (double) theItems.size());
        }

        Collections.sort(retVal, new MagnitudeComparator());

        return retVal;
    }

    private LinkMagnitude findLink(final String key,
            final Collection<LinkMagnitude> theMagnitudes) {
        LinkMagnitude retVal = (LinkMagnitude) CollectionUtils.find(theMagnitudes, new Predicate() {

            @Override
            public boolean evaluate(Object o) {
                LinkMagnitude lm = (LinkMagnitude) o;
                return lm.getLink().equalsIgnoreCase(key);
            }
        });

        return retVal;
    }

    public List<WebLinkDataItem> getItems() {
        List<WebLinkDataItem> retVal = null;

        if (items != null) {
            retVal = new ArrayList<WebLinkDataItem>();
            retVal.addAll(items);
        }

        return retVal;
    }

    public void setCenter(LinkMagnitudeVector center) {
        this.center = center;
    }

    @Override
    public void clearItems() {
        this.items.clear();
    }

    @Override
    public List<WebLinkDataItem> getDataItems() {
        return getItems();
    }

    @Override
    public String getTitle() {
        String retVal = null;
        if ((this.getItems() != null) && (this.getItems().size() > 0)) {
            WebLinkDataItem theDataItem = this.getItems().get(0);
            if (theDataItem != null) {
                String theTitle = theDataItem.getSource();

                if (theTitle != null) {
                    retVal = theTitle;
                }
            }
        }

        return retVal;
    }

    @Override
    public void hierCluster(Clusterer theClusterer) {
        if (theClusterer != null) {
            PageLinksDataSetManagerImpl pageSubSet = new PageLinksDataSetManagerImpl();
            pageSubSet.createFromItems(items);
            try {
                List<WebLinkDataItem> spList = pageSubSet.calculateLinkMagnitudes();
            } catch (Exception ex) {
                Logger.getLogger(ClusterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (this.items != null) {
                if (this.items.size() > CLUSTER_NO) {
                    theClusterer.setDataSet(this.items);
                    subClusters = theClusterer.cluster();

                    for (WebLinkCluster theSubCluster : subClusters) {
                        theSubCluster.hierCluster(theClusterer);
                    }
                } else {
                    if (this.items.size() > 1) {
                        subClusters = new ArrayList<WebLinkCluster>();
                        for (WebLinkDataItem theItem : items) {
                            WebLinkCluster theSubCluster = new ClusterImpl(0, theItem);
                            boolean add = subClusters.add(theSubCluster);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<WebLinkCluster> getSubClusters() {
        List<WebLinkCluster> theSubClusters = null;

        if (subClusters != null) {
            theSubClusters = new ArrayList<WebLinkCluster>();
            theSubClusters.addAll(subClusters);
        }

        return theSubClusters;
    }

    @Override
    public Set<WebLinkDataItem> getElements() {
        Set<WebLinkDataItem> retVal = new HashSet<WebLinkDataItem>();

        if (items != null) {
            retVal.addAll(items);
        }

        return retVal;
    }

    @Override
    public void setClusterId(int newId) {
        clusterId = newId;
    }

    @Override
    public WebLinkCluster copy() {
        ClusterImpl copy = new ClusterImpl(clusterId);
        copy.setCenter(getCenter());
        copy.setItems(getItems());
        copy.setSubClusters(getSubClusters());

        return copy;
    }

    @Override
    public String getElementsAsString() {
        StringBuilder buf = new StringBuilder();
        for (WebLinkDataItem e : items) {
            if (buf.length() > 0) {
                buf.append(",\n");
            }
            buf.append(e.getSource());
        }

        return "{" + buf.toString() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClusterImpl other = (ClusterImpl) obj;
        if (items == null) {
            if (other.items != null) {
                return false;
            }
        } else {
            if (!items.equals(other.items)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.clusterId;
        return hash;
    }

    private void setItems(List<WebLinkDataItem> items) {
        this.items = items;
    }

    private void setSubClusters(List<WebLinkCluster> subClusters) {
        this.subClusters = subClusters;
    }

    // should check if cluster exists already
    // should check that all of the parents data items 
    // are included (precon)
    @Override
    public void addSubCluster(WebLinkCluster cluster) {
        if (subClusters == null) {
            subClusters = new ArrayList<WebLinkCluster>();
        }

        subClusters.add(cluster);
    }

    @Override
    public String asXML() {
        StringBuilder theBuilder = new StringBuilder();
        theBuilder.append("<cluster>\n");
        theBuilder.append("<name>");
        Set<WebLinkDataItem> theElements = getElements();
        int noOfElements = theElements.size();

        if (noOfElements > 1) {
            theBuilder.append(Integer.toString(getClusterId()));
        } else {
            theBuilder.append(getElementsAsString());
        }
        theBuilder.append("</name>\n");
        if (subClusters == null) {
            if (noOfElements > 1) {
                for (WebLinkDataItem theItem : theElements) {
                    theBuilder.append("<cluster>\n");
                    theBuilder.append("<name>{").append(theItem.getSource()).append("}\n");
                    theBuilder.append("</name>\n");
                    theBuilder.append("</cluster>\n");
                }
            }
        } else {
            for (WebLinkCluster subCluster : subClusters) {
                theBuilder.append(subCluster.asXML());
            }
        }
        theBuilder.append("</cluster>\n");
        String theXML = theBuilder.toString();
        return theXML;
    }

    @Override
    public Set<WebLinkDataItem> getAllItems() {
        Set<WebLinkDataItem> retVal = new HashSet<WebLinkDataItem>();
        retVal.addAll(this.getElements());

        List<WebLinkCluster> theSubClusters = getSubClusters();

        if (theSubClusters != null) {
            for (WebLinkCluster theSubCluster : theSubClusters) {
                Set<WebLinkDataItem> subDataItems = theSubCluster.getAllItems();
                retVal.addAll(subDataItems);
            }
        }

        return retVal;
    }
}
