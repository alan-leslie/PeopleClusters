package peopleclusters;

import pagelinks.PageLinksDataSetManagerImpl;
import cluster.WebLinkCluster;
import cluster.WebLinkKMeansClustererImpl;
import pagelinks.WebLinkDataItem;
import iweb2.clustering.hierarchical.Dendrogram;
import iweb2.clustering.rock.LinkMatrix;
import iweb2.clustering.rock.ROCKAlgorithm;
import peopleclustrs.utils.DumpFile;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author al
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    // TODO - thread generate graph
    // only draw graph after layout adjustments are complete
    // set size and zoom depending on number of vertices
    // play with FRLayout setting to clarify large number of vertices
    public static void main(String[] args) {
        Properties properties = new Properties();
        FileInputStream is = null;

        try {
            is = new FileInputStream("PeopleClusters.properties");
            properties.load(is);
        } catch (IOException e) {
            // ...
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    /* .... */
                }
            }
        }

        Logger theLogger = Main.makeLogger();

        try {
            String linkFileName = properties.getProperty("LinkFileName", "cluster4.psv");
            String strPeopleOnly = properties.getProperty("PeopleOnly", "false");
            boolean peopleOnly = strPeopleOnly.equalsIgnoreCase("true");
            PageLinksDataSetManagerImpl pt = new PageLinksDataSetManagerImpl();
            String isPersonFileName = properties.getProperty("IsPersonFileName", "peeps_classify.txt");
            String dumpFileName = properties.getProperty("DumpFileName");
            String strNoOfClusters = properties.getProperty("NoOfClusters", "10");
            int noOfClusters = Integer.parseInt(strNoOfClusters);
            String strNoOfIterations = properties.getProperty("NoOfIterations", "50");
            int noOfIterations = Integer.parseInt(strNoOfIterations);
            String strClusterType = properties.getProperty("ClusterType", "KMeans");

            pt.createFromFile(linkFileName, peopleOnly, isPersonFileName);

            if (dumpFileName != null
                    && !dumpFileName.isEmpty()) {
                pt.dumpLinks(dumpFileName);
            }

            List<WebLinkDataItem> theItems = pt.calculateLinkMagnitudes();
            List<WebLinkDataItem> testData = new ArrayList<WebLinkDataItem>();

            for (WebLinkDataItem theItem : theItems) {
                if (theItem.getLinks().isEmpty()) {
                    theLogger.log(Level.WARNING, "{0} has no items", theItem.getSource());
                } else {
                    testData.add(theItem);
                }
            }

            double minCost = Double.MAX_VALUE;
            double maxCost = -1.0;

            if (strClusterType.equalsIgnoreCase("KMeans")) {
                WebLinkKMeansClustererImpl clusterer = new WebLinkKMeansClustererImpl(noOfClusters);

                clusterer.setDataSet(testData);

                for (int i = 0; i < noOfIterations; ++i) {
                    List<WebLinkCluster> clusters = clusterer.cluster();

                    double cost = clusterer.costFunction();

                    for (WebLinkCluster theCluster : clusters) {
                        int noOfElements = theCluster.getElements().size();
                        System.out.println("Cluster: " + theCluster.getTitle() + "item count = " + Integer.toString(noOfElements));
                    }

                    if (cost < minCost) {
                        Dendrogram dnd = new Dendrogram("TopLevel");
                        dnd.addLevel(String.valueOf(cost), clusters);

                        DumpFile.writeXML("KMeansMin.xml", dnd.asXML());
                        minCost = cost;
                    }

                    if (cost > maxCost) {
                        Dendrogram dnd = new Dendrogram("TopLevel");
                        dnd.addLevel(String.valueOf(cost), clusters);

                        DumpFile.writeXML("KMeansMax.xml", dnd.asXML());
                        maxCost = cost;
                    }
                    
                    System.out.println("Max cost = " + String.valueOf(maxCost));
                    System.out.println("Min cost = " + String.valueOf(minCost));
                }
            } else {
                double th = 0.15;
                ROCKAlgorithm rock = new ROCKAlgorithm(testData, noOfClusters, th);
                LinkMatrix linkMatrix = rock.getLinkMatrix();
                linkMatrix.printSimilarityMatrix();
                linkMatrix.printPointNeighborMatrix();
                linkMatrix.printPointLinkMatrix();

                Dendrogram dnd = rock.cluster();

//            dnd.printAll();
                DumpFile.writeXML("ROCKTest.xml", dnd.asXML());
            }
        } catch (Exception ex) {
            theLogger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return - valid logger (single file).
     */
    private static Logger makeLogger() {
        Logger lgr = Logger.getLogger("PeopleCluster");
        lgr.setUseParentHandlers(false);
        lgr.addHandler(simpleFileHandler());
        return lgr;
    }

    /**
     *
     * @return - valid file handler for logger.
     */
    private static FileHandler simpleFileHandler() {
        try {
            FileHandler hdlr = new FileHandler("PeopleCluster.log");
            hdlr.setFormatter(new SimpleFormatter());
            return hdlr;
        } catch (Exception e) {
            System.out.println("Failed to create log file");
            return null;
        }
    }
}
