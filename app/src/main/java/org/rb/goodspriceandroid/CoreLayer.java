package org.rb.goodspriceandroid;

import android.util.Log;
import org.rb.goodspriceandroid.control.DataControl;
import org.rb.goodspriceandroid.io.FileIOImpl;
import org.rb.goodspriceandroid.io.PersistenceManager;
import org.rb.goodspriceandroid.model.Good;


import java.io.IOException;

/**
 * Created by raitis on 23-Feb-17.
 * <pre>
 * SingleTone class
 * To use CoreLayer InitCore method has to be called in the first time
 * </pre>
 */
public class CoreLayer {
    private final static String GOODSDATAFILE="Goods.bin";
    private static final CoreLayer _Instance = new CoreLayer();

    public static CoreLayer getInstance() {
        return _Instance;
    }

    private FileIOImpl<Good> fileIO;
    private PersistenceManager<Good> pm;
    private DataControl control;
    private boolean newDataImported;
    /**
     * Init core data access and control classes
     * @param dataFilePath full path of datafile (file location in the application's private directory )
     */
    public void InitCore(String dataFilePath){
        try {

            fileIO = new FileIOImpl<>(dataFilePath+GOODSDATAFILE);
            Log.d("=====Init file",dataFilePath+GOODSDATAFILE);
        } catch (IOException e) {

            Log.e(getClass().getName()+":Init fileIO",Log.getStackTraceString(e));
        }

        pm = new PersistenceManager<>(fileIO);

        control = null;
        try {
            control = new DataControl(pm, null);
        } catch (IOException e) {
            Log.e(getClass().getName()+":Init Control",Log.getStackTraceString(e));
        } catch (ClassNotFoundException e) {
            Log.e(getClass().getName()+":Init Control",Log.getStackTraceString(e));
        }
        newDataImported=false;

        Log.d(getClass().getName()+"Init Core","Init completed...");
    }

    public DataControl getControl() {
        return control;
    }

    public boolean isNewDataImported() {
        return newDataImported;
    }

    public void setNewDataImported(boolean newDataImported) {
        this.newDataImported = newDataImported;
    }

    private CoreLayer() {

    }


}
