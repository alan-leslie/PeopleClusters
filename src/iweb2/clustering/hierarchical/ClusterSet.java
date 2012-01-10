package iweb2.clustering.hierarchical;


//import iweb2.ch4.model.Cluster;
//import iweb2.ch4.model.MultiDimensionalDataPoint;

import cluster.WebLinkCluster;
import pagelinks.WebLinkDataItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set of clusters.
 */
public class ClusterSet {

    private Set<WebLinkCluster> allClusters = new HashSet<WebLinkCluster>();
    
    public WebLinkCluster findClusterByElement(WebLinkDataItem e) {
        WebLinkCluster cluster = null;
        for(WebLinkCluster c : allClusters) {
            // todo sort his out
//            if( c.contains(e) ) {
//                cluster = c;
//                break;
//            }
        }
        return cluster;
    }

    public List<WebLinkCluster> getAllClusters() {
        return new ArrayList<WebLinkCluster>(allClusters);
    }
    
    public boolean add(WebLinkCluster c) {
        return allClusters.add(c);
    }
    
    public boolean remove(WebLinkCluster c) {
        return allClusters.remove(c);
    }
    
    public int size() {
        return allClusters.size();
    }
    
//    public ClusterSet copy() {
//        ClusterSet clusterSet = new ClusterSet();
//        for(Cluster c : this.allClusters ) {
//            Cluster clusterCopy = c.copy();
//            clusterSet.add(clusterCopy);
//        }
//        return clusterSet;
//    }
}
