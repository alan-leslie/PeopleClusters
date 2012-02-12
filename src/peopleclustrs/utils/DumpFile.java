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
                for(String theTarget: theItem.getLinks()){
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
}
