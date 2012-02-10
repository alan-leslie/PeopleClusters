package peopleclusters;

import pagelinks.PageLinksDataSetManagerImpl;
import cluster.WebLinkCluster;
import pagelinks.WebLinkDataItem;
import iweb2.clustering.hierarchical.Dendrogram;
import iweb2.clustering.rock.LinkMatrix;
import iweb2.clustering.rock.ROCKAlgorithm;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;


/**
 *
 * @author al
 */
public class ROCKAlgorithmTest {

    @Test
    public void testBasicDatItems() {
        //Define data
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
                try {
                    PageLinksDataSetManagerImpl pt = new PageLinksDataSetManagerImpl();
                    pt.createFromFile("cluster4.psv", false, "");

                    List<WebLinkDataItem> theItems = pt.calculateLinkMagnitudes();

//                    WebLinkDataItem[] testData = (WebLinkDataItem[]) theItems.toArray(new WebLinkDataItem[theItems.size()]);

                    int k = 1;
                    double th = 0.2;
                    ROCKAlgorithm rock = new ROCKAlgorithm(theItems, k, th);
                    LinkMatrix linkMatrix = rock.getLinkMatrix();
                    linkMatrix.printSimilarityMatrix();
                    linkMatrix.printPointNeighborMatrix();
                    linkMatrix.printPointLinkMatrix();

                    int links = linkMatrix.getLinks(theItems.get(0), theItems.get(1));
                    assert (links == 2);
                    links = linkMatrix.getLinks(theItems.get(0), theItems.get(2));
                    assert (links == 3);
                    links = linkMatrix.getLinks(theItems.get(0), theItems.get(3));
                    assert (links == 3);
                    links = linkMatrix.getLinks(theItems.get(1), theItems.get(2));
                    assert (links == 3);
                    links = linkMatrix.getLinks(theItems.get(1), theItems.get(3));
                    assert (links == 3);
                    links = linkMatrix.getLinks(theItems.get(2), theItems.get(3));
                    assert (links == 4);

                    Dendrogram dnd = rock.cluster();

                    List<Integer> theLevels = dnd.getAllLevels();
                    assert (theLevels.size() == 4);
                    List<WebLinkCluster> theClusters = dnd.getClustersForLevel(1);
                    assert (theClusters.size() == theItems.size());
                    theClusters = dnd.getClustersForLevel(2);
                    assert (theClusters.size() == 3);
                    theClusters = dnd.getClustersForLevel(3);
                    assert (theClusters.size() == 2);
                    theClusters = dnd.getClustersForLevel(4);
                    assert (theClusters.size() == 1);

                    dnd.printAll();
                } catch (Exception ex) {
                    Logger.getLogger(ROCKAlgorithmTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
//        });
//    }
}
