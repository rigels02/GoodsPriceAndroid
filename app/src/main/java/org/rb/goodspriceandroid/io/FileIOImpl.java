
package org.rb.goodspriceandroid.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class FileIOImpl<T> implements IFileIO<T> {

    private String readWriteFile;
    private final static String MAGICSIGN="29-01-2017@#$%";
    private Timestamp timeStamp;
    
    public FileIOImpl(String readWriteFile) throws IOException {
        this.readWriteFile = readWriteFile;

        File file = new File(readWriteFile);
        if( !file.exists()){
            file.createNewFile();
            List<T> ListOfObjects= new ArrayList<>();
            saveEmptyList(ListOfObjects);
        }
    }

    public String getFilePath(){
        return readWriteFile;
    }
     private void saveEmptyList(List<T> ListOfObjects) throws IOException {
         saveList(ListOfObjects);
     }
    
     /**
      * Get file size
      * @param filePath
      * @return file size or zerro if not exists.
      */
    public long getFileSize(String filePath){
        File file = new File(filePath);
        if( file.exists()){
          return file.length();
        }else{
          return 0;
        }
     }
    
    @Override
    public void saveList(List<T> ListOfObjects) throws IOException {
        ObjectOutputStream oos;

        oos = openOutStream();
        if (oos != null) {
            oos.writeObject(ListOfObjects);

        }
        if (oos != null) {
            try {
                oos.flush();
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public List<T> readListStream() throws IOException, ClassNotFoundException {
       List<T> ListOfObjects=null;


        ObjectInputStream ois = openInStream();
        if(ois != null){
            ListOfObjects = (List<T>) ois.readObject();
            ois.close();
        }

        return ListOfObjects;    
    }

    /**
     * Write file's heade info
     * magicsign and timestamp 
     * @param oos
     * @throws IOException 
     */
    private void writeHeader(ObjectOutputStream oos) throws IOException{
        oos.writeUTF(MAGICSIGN);
        //TimeStamp is in format 2016-11-16 06:43:19.77
        oos.writeObject( timeStamp = new Timestamp(System.currentTimeMillis()));
        
        
    }
    /**
     * Read file's header info
     *  magicsign and timestamp 
     * @param ois 
     */
    private void readHeader(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        String magic = ois.readUTF();
        if( !magic.equals(MAGICSIGN)){
            throw new IOException("Bad input file's header!");
        }
        timeStamp= (Timestamp) ois.readObject();
        
    }
    
    private ObjectOutputStream openOutStream() throws IOException {
        ObjectOutputStream oos = null;
        if (this.readWriteFile != null) {

            try {
                oos = new ObjectOutputStream(new FileOutputStream(readWriteFile));
                writeHeader(oos);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } 
        }
        return oos;
    }

    private ObjectInputStream openInStream() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        if (this.readWriteFile != null) {

            try {
                ois = new ObjectInputStream(new FileInputStream(readWriteFile));
                readHeader(ois);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } 
        }
        return ois;
    }

    @Override
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    

   
    
}
