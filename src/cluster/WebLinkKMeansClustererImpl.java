package cluster;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pagelinks.WebLinkDataItem;
import java.util.*;
import pagelinks.PageLinksDataSetManagerImpl;

public class WebLinkKMeansClustererImpl implements Clusterer {

    private List<WebLinkDataItem> textDataSet = null;
    private List<WebLinkCluster> clusters = null;
    private int numClusters;

    public WebLinkKMeansClustererImpl(List<WebLinkDataItem> textDataSet,
            int numClusters) {
        this.textDataSet = textDataSet;
        this.numClusters = numClusters;
    }

    public WebLinkKMeansClustererImpl(int numClusters) {
        this.numClusters = numClusters;
    }

    @Override
    public void setDataSet(List<WebLinkDataItem> textDataSet) {
        this.textDataSet = textDataSet;
    }

    @Override
    public List<WebLinkCluster> cluster() {
        if (this.textDataSet.isEmpty()) {
            return Collections.emptyList();
        }
        this.intitializeClusters();
        boolean change = true;
        int count = 0;
        while ((count++ < 100) && (change)) {
            clearClusterItems();
            change = reassignClusters();
            computeClusterCenters();
        }
        return this.clusters;
    }

    private void intitializeClusters() {
        this.clusters = new ArrayList<WebLinkCluster>();

        if (numClusters > textDataSet.size()) {
            for (int i = 0; i < this.numClusters; i++) {
                ClusterImpl cluster = new ClusterImpl(i);
                cluster.setCenter(textDataSet.get(i).getLinkMagnitudeVector());
                this.clusters.add(cluster);
            }
        } else {
            Map<Integer, Integer> usedIndexes = new HashMap<Integer, Integer>();
            for (int i = 0; i < this.numClusters; i++) {
                ClusterImpl cluster = new ClusterImpl(i);
                cluster.setCenter(getDataItemAtRandom(usedIndexes).getLinkMagnitudeVector());
                this.clusters.add(cluster);
            }
        }
    }

    private WebLinkDataItem getDataItemAtRandom(Map<Integer, Integer> usedIndexes) {
        boolean found = false;
        while (!found) {
            double dataSetSize = (double) this.textDataSet.size();
            double theRandomNo = Math.random() * dataSetSize;
            int index = (int) Math.floor(theRandomNo);
            if (!usedIndexes.containsKey(index)) {
                System.out.println(index);
                usedIndexes.put(index, index);
                return this.textDataSet.get(index);
            }
        }
        return null;
    }

    private boolean reassignClusters() {
        int numChanges = 0;
        for (WebLinkDataItem item : this.textDataSet) {
            WebLinkCluster newCluster = getClosestCluster(item);
            if ((item.getClusterId() == null)
                    || (item.getClusterId().intValue()
                    != newCluster.getClusterId())) {
                System.out.println("From " + item.getClusterId()
                        + " to " + newCluster.getClusterId());
                numChanges++;
                item.setClusterId(newCluster.getClusterId());
            }
            newCluster.addDataItem(item);
        }
        System.out.println("Changes=" + numChanges);
        return (numChanges > 0);
    }

    private void computeClusterCenters() {
        for (WebLinkCluster cluster : this.clusters) {
            cluster.computeCenter();
        }
    }

    private void clearClusterItems() {
        for (WebLinkCluster cluster : this.clusters) {
            cluster.clearItems();
        }
    }

    private WebLinkCluster getClosestCluster(WebLinkDataItem item) {
        WebLinkCluster closestCluster = null;
        Double hightSimilarity = null;
        for (WebLinkCluster cluster : this.clusters) {
            double similarity =
                    cluster.getCenter().dotProduct(item.getLinkMagnitudeVector());
            if ((hightSimilarity == null)
                    || (hightSimilarity.doubleValue() < similarity)) {
                hightSimilarity = similarity;
                closestCluster = cluster;
            }
        }
        return closestCluster;
    }

    public double costFunction() {
        double retVal = 0.0;

        for (WebLinkCluster theCluster : clusters) {
            Set<WebLinkDataItem> theElements = theCluster.getElements();
            int internalLinks = 0;
            int externalLinks = 0;

            for (WebLinkDataItem theItem : theElements) {
                for (String theLink : theItem.getLinks()) {
                    boolean internalLinkFound = false;
                    for (WebLinkDataItem theComparisonItem : theElements) {
                        if (theComparisonItem.getSource().equalsIgnoreCase(theLink)) {
                            internalLinkFound = true;
                        }
                    }

                    if (internalLinkFound) {
                        ++internalLinks;
                    } else {
                        ++externalLinks;
                    }
                }
            }

            if (externalLinks == 0) {
                externalLinks = 1;
                internalLinks *= 2;
            }

            retVal += (double) internalLinks / (double) externalLinks;
        }

        return retVal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (WebLinkCluster cluster : clusters) {
            sb.append("\n\n");
            sb.append(cluster.toString());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Properties properties = new Properties();

        try {
            PageLinksDataSetManagerImpl pt = new PageLinksDataSetManagerImpl();
            //pt.createFromFile("cluster4.psv", false, "");
            String linkFileName = properties.getProperty("LinkFileName", "cluster4.psv");
            String strPeopleOnly = properties.getProperty("PeopleOnly", "false");
            boolean peopleOnly = strPeopleOnly.equalsIgnoreCase("true");
            //PageLinksDataSetManagerImpl pt = new PageLinksDataSetManagerImpl();
            String isPersonFileName = properties.getProperty("IsPersonFileName", "peeps_classify.txt");
            String dumpFileName = properties.getProperty("DumpFileName");
            pt.createFromFile(linkFileName, peopleOnly, isPersonFileName);


            WebLinkKMeansClustererImpl clusterer = new WebLinkKMeansClustererImpl(2);
            List<WebLinkCluster> clusters = clusterer.cluster();
            //           WebLinkCluster rootCluster = new ClusterImpl(0, pt);
            //           rootCluster.hierCluster(clusterer);
            //            System.out.println(rootCluster.getSubClusters().toString());

            for (WebLinkCluster theCluster : clusters) {
                System.out.println(theCluster.getTitle());
            }
        } catch (IOException ex) {
            Logger.getLogger(WebLinkKMeansClustererImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
