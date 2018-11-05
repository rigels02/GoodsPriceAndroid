/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rb.goodspriceandroid.io;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Developer
 * @param <T>
 */
public interface IPersistenceManager<T> {

    /**
     * Read entities from DB (JPA)
     * @return
     */
    List<T> readListDB();

    /**
     * Read list from Stream (File)
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
     List<T> readListStream() throws IOException, ClassNotFoundException;

    /**
     * Save List of entities into both Stream (File) or DB
     * @param ListOfObjects
     * @throws IOException
     */
     void saveList(List<T> ListOfObjects) throws IOException;

    /**
     * Get data modification time Stamp
     * @return time stamp
     */
    Date getTimeStamp();
}
