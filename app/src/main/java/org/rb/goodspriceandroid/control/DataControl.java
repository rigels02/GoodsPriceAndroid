package org.rb.goodspriceandroid.control;


import org.rb.goodspriceandroid.impoexpo.IExportImport;
import org.rb.goodspriceandroid.io.IPersistenceManager;
import org.rb.goodspriceandroid.model.Good;
import org.rb.goodspriceandroid.views.IListView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <pre>
 * Only goods List modify methods are synchronized, but no read .
 * Exception: The exportData() is synchronized too.
 * </pre>
 * @author Developer
 * 
 */
public class DataControl implements IDataControl {

    private IPersistenceManager<Good> pm;
    private IListView lv;
    private List<Good> goods;
    private IExportImport expImp;
    
    public DataControl(IPersistenceManager<Good> pm, IListView lv) throws IOException, ClassNotFoundException {

        this.pm = pm;
        this.lv = lv;
        if (pm != null) {
            goods = pm.readListStream();
        } else {
            goods = new ArrayList<>();
        }
    }

  
    public void setExpImp(IExportImport expImp) {
        this.expImp = expImp;
    }

    private void saveAndUpdateView() throws IOException {
        if (pm != null) {
            pm.saveList(goods);
        }
        if (lv != null) {
        lv.updateListView(goods);
        }
    }

    
    @Override
    public Good getItem(int id) throws IndexOutOfBoundsException{
        //checkIndex(id);
        return goods.get(id);
    }
    
    @Override
    public synchronized  void add(Good good) throws IOException {
        if(goods.size()>= (Integer.MAX_VALUE - 8)){
            throw new IOException("MAXIMUM alowed goods number has been reached!!!");
         }
        goods.add(good);
        sortByDate();
        saveAndUpdateView();

    }

    @Override
    public synchronized void del(int id) throws IOException,IndexOutOfBoundsException {
        goods.remove(id);
        sortByDate();
        saveAndUpdateView();
        
    }

    @Override
    public synchronized void edit(int id, Good good) throws IOException {
        goods.set(id, good);
        sortByDate();
        saveAndUpdateView();
    }

    /**
     * This method get goods List from storage. This implementation
     * does not update goods list field of DataControl.
     * It is considered that both list on storage and in the DataControl are
     * already synchronized .
     * 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    @Override
    public List<Good> getList() throws IOException, ClassNotFoundException {
        if (pm != null) {
            return pm.readListStream();
        } else {
            return goods;
        }

    }

    @Override
    public Date getTimeStamp() {
        if(pm != null){
            return pm.getTimeStamp();
        }
        return null;
    }

    /**
     * We do not need always to read goods list from persistence (file,DB).
     * For better performance we can use goods list kept in DataControl
     * memory.
     * @return 
     */
    @Override
    public synchronized List<Good> getListFromDataControlMemory(){
        
        /*try {
        List<Good> copy = cloneList();
        } catch (CloneNotSupportedException ex) {
        Logger.getLogger(DataControl.class.getName()).log(Level.SEVERE, null, ex);
        }*/
      return goods;
    }
   
    public synchronized void setListForDataControlMemory(List<Good> lst){
       goods= lst;
    }
    public synchronized void saveAllData() throws IOException{
      saveAndUpdateView();
    }
    private List<Good> cloneList() throws CloneNotSupportedException{
        ArrayList<Good> copy = new ArrayList<>();
        for (Good good : goods) {
           copy.add(good.makeCopy());
        }
        return copy;
    }
    
     private void sortByDate() {
        List<Good> sorted = new ArrayList<>(goods);

        Collections.sort(sorted, new Comparator<Good>() {
            @Override
            public int compare(Good o1, Good o2) {
                return o1.getCdate().compareTo(o2.getCdate());
            }
        });
        this.goods= sorted;
       
    }

     /**
      * Export List of Good as CSV to file filePath
      * @param filePath
      * @throws FileNotFoundException
      * @throws UnsupportedEncodingException 
      */
    @Override
    public void exportData(String filePath) throws FileNotFoundException, UnsupportedEncodingException{
        if(this.expImp != null){
            synchronized(this){
           expImp.exportData(filePath, goods);
            }
        }else {
            System.err.println("ERROR: goodsprice.control.DataControl.exportData(): ExpImp == NULL!");
        }
        
    }
    
    /**
     * Import List of Goods data from CSV file
     * @param filePath
     * @throws IOException
     * @throws Exception 
     */
    @Override
    public void importData(String filePath) throws IOException, Exception{
       if(this.expImp!=null){
           synchronized(this) {
         goods = expImp.importData(filePath);
         saveAndUpdateView();
           }
       }else {
           System.err.println("goodsprice.control.DataControl.importData(): ExpImp == NULL!");
        } 
    }
    @Override
    public void importData(List<Good> goods) throws IOException{
            synchronized(this) {
         this.goods = goods;
         saveAndUpdateView();
           }
    }
    
     //--- below are test simulation methods -----------------------
    @Override
    public void startPersistDataControlMemoryThread() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("PersistDataControlMemoryThread started: "
                        +Thread.currentThread().getId());
                persistDataControlMemory();
            }
        });
        t1.start();
    }

    /*
    Simulate persist data
    */
    @Override
    public synchronized void persistDataControlMemory() {
        System.out.println("persistDataControlMemory() called by thread: "+
                Thread.currentThread().getId());
        long startTime = System.currentTimeMillis();
        try {
            //simulate persistance to DB
            Thread.sleep(7000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DataControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("persistDataControlMemory(): Time = "+
                (System.currentTimeMillis()-startTime)+ " Done");
    }

    

    @Override
    public Good simulateAccessToDataControlMemoryGood(String txt) throws Exception {
      System.out.println("simulateAccessToDataControlMemoryGood() called by dialog thread: "+
                Thread.currentThread().getId());
        System.out.println("Received: "+txt);
        if(txt.equals("Exception")){
          throw new Exception(" Generated Exception !!!");
        }
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DataControl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        System.out.println("Delta Time: "+(System.currentTimeMillis()-startTime)/1000+" sec");
        return new Good(new Date(), "Aboli", "Rimi", 0, 0);
    }

}
