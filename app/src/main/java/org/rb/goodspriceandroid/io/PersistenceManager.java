package org.rb.goodspriceandroid.io;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Developer
 * @param <T>
 */
public class PersistenceManager<T> implements IPersistenceManager<T> {

   private IJpaRepository<T> jpaRepo;
   private IFileIO<T> fileIO;
   
    public PersistenceManager() {
    }

    public PersistenceManager(IJpaRepository<T> jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    public PersistenceManager(IFileIO<T> fileIO) {
        this.fileIO = fileIO;
    }

    public PersistenceManager(IJpaRepository<T> jpaRepo, IFileIO<T> fileIO) {
        this.jpaRepo = jpaRepo;
        this.fileIO = fileIO;
    }

 


    /**
     * Save List of entities into both Stream (File) or DB
     * @param ListOfObjects
     * @throws IOException 
     */
    @Override
    public void saveList(List<T> ListOfObjects) throws IOException {
        if (jpaRepo != null) {
            jpaRepo.saveList(ListOfObjects);
            
        }
        if(fileIO != null){
          fileIO.saveList(ListOfObjects);
        }
        
    }
    /**
     * Read entities from DB (JPA)
     * @return 
     */
    @Override
    public List<T> readListDB() {
       List<T> ListOfObjects=null;
        if (jpaRepo != null) {
            
            ListOfObjects= jpaRepo.readListDB();
        }

        
        return ListOfObjects;

    }
    
    /**
     * Read list from Stream (File)
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    @Override
    public List<T> readListStream() throws IOException, ClassNotFoundException  {
       List<T> ListOfObjects=null;
        
       if(fileIO != null){
         ListOfObjects = fileIO.readListStream();
       }
        
        return ListOfObjects;

    }


    @Override
    public Date getTimeStamp() {
        if( jpaRepo!= null){
            return jpaRepo.getTimeStamp();
        }
        if(fileIO != null){
            return fileIO.getTimeStamp();
        }
        return null;
    }
}
