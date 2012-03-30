package pagelinks;

import java.io.IOException;
import peopleclustrs.utils.InverseDocFreqEstimator;
import peopleclustrs.utils.CSVFile;
import peopleclustrs.utils.InverseDocFreqEstimatorImpl;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import peopleclustrs.utils.DumpFile;

/**
 *
 * @author al
 */
public class PageLinksDataSetManagerImpl implements DataSetManager {

    private List<WebLinkDataItem> peopleLinksList = null;
    Map<String, PageLinks> peopleLinks = null;
    private Set<String> isPersonSet = null;

    public void dumpLinks(String dumpFileName) {
        List<PageLinks> theLinks = new ArrayList<PageLinks>();
        
        for (Map.Entry<String, PageLinks> entry : peopleLinks.entrySet()) {
            theLinks.add(entry.getValue());
        }

        DumpFile.writeLinks(dumpFileName, theLinks);
    }

    public void createFromFile(String linkFileName,
            boolean peopleOnly,
            String theIsPersonFileName) throws IOException {
        List<String[]> fileData = CSVFile.getFileData(linkFileName, "\\|");

        isPersonSet = new TreeSet<String>();


        if (peopleOnly) {
         List<String> isPersonData = CSVFile.getFileLines(theIsPersonFileName);
         
         for (String person : isPersonData) {
                isPersonSet.add(person);
            }
        }

        peopleLinks = generateMap(fileData, peopleOnly);
        peopleLinksList = getData();
    }

    public void createFromItems(List<WebLinkDataItem> newLinks) {
        peopleLinksList = newLinks;
    }

    private Map<String, PageLinks> generateMap(List<String[]> fileData,
            boolean peopleOnly) {
        Map<String, PageLinks> retVal = new HashMap<String, PageLinks>();

        for (String[] theLinkPair : fileData) {
            String theSource = theLinkPair[0];
            PageLinks theLinks = null;
            boolean shouldAdd = true;
            
            if(peopleOnly && !isPersonSet.contains(theSource)){
                shouldAdd = false;
            }

            if (shouldAdd) {
                if (!retVal.containsKey(theSource)) {
                    theLinks = new PageLinks(theSource);
                    retVal.put(theSource, theLinks);
                }
            }
        }

        for (String[] theLinkPair : fileData) {
            String theSource = theLinkPair[0];
            String theTarget = theLinkPair[1];
            LinkMagnitude theMag = new LinkMagnitudeImpl(theTarget, 1.0);
            PageLinks theLinks = null;
            boolean shouldAddLink = true;

            if (peopleOnly){
                if(!isPersonSet.contains(theTarget)) {
                    shouldAddLink = false;
                }
            }

            if (retVal.containsKey(theSource)) {
                if (shouldAddLink) {
                    theLinks = retVal.get(theSource);
                    theLinks.addLink(theMag);
                }
            }
        }

        return retVal;
    }

    private List<WebLinkDataItem> getData() {
        List<WebLinkDataItem> retVal = new ArrayList<WebLinkDataItem>();

        for (Map.Entry<String, PageLinks> entry : peopleLinks.entrySet()) {
            retVal.add(entry.getValue());
        }

        return retVal;
    }

    @Override
    public List<WebLinkDataItem> calculateLinkMagnitudes() {
        List<WebLinkDataItem> result = new ArrayList<WebLinkDataItem>();
        InverseDocFreqEstimator freqEstimator =
                new InverseDocFreqEstimatorImpl(peopleLinksList.size());

        for (WebLinkDataItem thePage : peopleLinksList) {
            for (int index = 0; index < thePage.numLinks(); ++index) {
                String theLink = thePage.getLinkAt(index);
                freqEstimator.addCount(theLink);
            }
        }

        System.out.println("No of links is:" + Integer.toString(freqEstimator.noOfLinks()));
        System.out.println("Frequencies");
        freqEstimator.outputFrequencies();
        freqEstimator.outputInverseFrequencies();
        System.out.println("");
//        freqEstimator.prune(10, 90);

        for (WebLinkDataItem thePage : peopleLinksList) {
            PageLinks adjustedLinks = new PageLinks(thePage.getSource());

            for (int index = 0; index < thePage.numLinks(); ++index) {
                double mag = 0.0;

                if (freqEstimator.hasValid(thePage.getLinkAt(index))) {
                    mag = 1.0;
                }

                LinkMagnitude theLink = new LinkMagnitudeImpl(thePage.getLinkAt(index), mag);

                adjustedLinks.addLink(theLink);
            }

            result.add(adjustedLinks);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        PageLinksDataSetManagerImpl pt = new PageLinksDataSetManagerImpl();
        pt.createFromFile("cluster4.psv", false, "");

        List<WebLinkDataItem> beList = pt.calculateLinkMagnitudes();

        for (WebLinkDataItem theItem : beList) {
            System.out.println("Source:" + theItem.getSource());

            System.out.println("Targets:");
            List<String> theTargets = theItem.getLinks();

            for (String theLink : theTargets) {
                System.out.println(theLink);
            }

            // magnitudes in a vector shoul be normalized
            LinkMagnitudeVector linkMagnitudeVector = theItem.getLinkMagnitudeVector();
            List<LinkMagnitude> theMags = linkMagnitudeVector.getLinkMagnitudes();

            for (LinkMagnitude theMag : theMags) {
                System.out.println(theMag);
            }
        }
    }
}
