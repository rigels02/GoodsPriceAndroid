
package org.rb.goodspriceandroid.io;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Developer
 * @param <T>
 */
public interface IJpaRepository<T> {
    
  
     /**
     * Save List of entities into DB (Jpa)
     * @param ListOfObjects
     * @throws IOException 
     */
     void saveList(List<T> ListOfObjects) throws IOException;
    
    /**
     * Read entities from DB (JPA)
     * @return 
     */
    List<T> readListDB();

    /**
     * Get modified data time stamp
     * @return time stamp
     */
    Date getTimeStamp();
}
