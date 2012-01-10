package peopleclusters;

import pagelinks.DataSetManager;
import pagelinks.PageLinksDataSetManagerImpl;
import cluster.WebLinkCluster;
import pagelinks.WebLinkDataItem;
import iweb2.clustering.hierarchical.Dendrogram;
import iweb2.clustering.rock.LinkMatrix;
import iweb2.clustering.rock.ROCKAlgorithm;
import peopleclustrs.utils.XMLFile;
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
            PageLinksDataSetManagerImpl pt = new PageLinksDataSetManagerImpl();
            pt.createFromFile(linkFileName);
            
            List<WebLinkDataItem> theItems = pt.calculateLinkMagnitudes();
            List<WebLinkDataItem> testData = new ArrayList<WebLinkDataItem>();
            
            for(WebLinkDataItem theItem: theItems){
                if(theItem.getLinks().isEmpty()){
                    theLogger.log(Level.WARNING, "{0} has no items", theItem.getSource());
                } else {
                    testData.add(theItem);
                }
            }

            int k = 1;
            double th = 0.2;
            ROCKAlgorithm rock = new ROCKAlgorithm(testData, k, th);
            LinkMatrix linkMatrix = rock.getLinkMatrix();
            linkMatrix.printSimilarityMatrix();
            linkMatrix.printPointNeighborMatrix();
            linkMatrix.printPointLinkMatrix();

            Dendrogram dnd = rock.cluster();

            dnd.printAll();
            XMLFile.writeXML("ROCKTest.xml", dnd.asXML());
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
