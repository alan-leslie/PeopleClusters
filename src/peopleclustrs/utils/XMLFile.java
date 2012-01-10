/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peopleclustrs.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author al
 */
public class XMLFile {
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
}
