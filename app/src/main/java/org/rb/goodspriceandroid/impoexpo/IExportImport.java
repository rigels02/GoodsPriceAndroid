
package org.rb.goodspriceandroid.impoexpo;


import org.rb.goodspriceandroid.model.Good;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * @author Developer
 */
public interface IExportImport {

    /**
     * Export data to file filePath
     * @param filePath
     * @param goods
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException 
     */
     void exportData(String filePath, List<Good> goods) throws FileNotFoundException, UnsupportedEncodingException;

    /**
     * Import data from file filePath
     * @param filePath
     * @return List of Good
     * @throws IOException
     * @throws Exception 
     */
     List<Good> importData(String filePath) throws IOException, Exception;
    
}
