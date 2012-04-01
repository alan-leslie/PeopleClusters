/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peopleclustrs.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import pagelinks.PageLinks;

/**
 *
 * @author al
 */
public class DumpFile {

    public static void writeXML(String fileName,
            String theXML) {
        FileWriter theWriter = null;
        try {
            theWriter = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(theWriter);
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            out.write(theXML);
            out.flush();
        } catch (IOException e) {
            // ...
        } finally {
            if (null != theWriter) {
                try {
                    theWriter.close();
                } catch (IOException e) {
                    /* .... */
                }
            }
        }
    }

    public static void writeLinks(String fileName,
            List<PageLinks> theLinks) {
        FileWriter theWriter = null;
        try {
            theWriter = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(theWriter);

            for (PageLinks theItem : theLinks) {
                for (String theTarget : theItem.getLinks()) {
                    String outputStr = theItem.getSource() + ", " + theTarget;
                    out.write(outputStr);
                    out.newLine();
                }
            }

            out.flush();
        } catch (IOException e) {
            // ...
        } finally {
            if (null != theWriter) {
                try {
                    theWriter.close();
                } catch (IOException e) {
                    /* .... */
                }
            }
        }
    }

    public static void writeCosts(String fileName,
            List<double[]> theCosts) {
        FileWriter theWriter = null;
        try {
            theWriter = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(theWriter);

            int noOfClusters = 10;
            for (double[] theCostRange : theCosts) {
                out.write("Cluster no = " + String.valueOf(noOfClusters) + "\n");
                out.write("Min = " + String.valueOf(theCostRange[0]) + "\n");
                out.write("Max = " + String.valueOf(theCostRange[1]) + "\n");
                out.flush();

                noOfClusters += 5;
            }

        } catch (IOException e) {
            // ...
        } finally {
            if (null != theWriter) {
                try {
                    theWriter.close();
                } catch (IOException e) {
                    /* .... */
                }
            }
        }
    }

    public static void writeAverages(String string, List<double[]> theCosts) {
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void writeCentres(String string, List<double[]> theCosts) {
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
