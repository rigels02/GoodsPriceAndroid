package org.rb.goodspriceandroid.impoexpo;


import org.rb.goodspriceandroid.io.TxtReaderWriter;
import org.rb.goodspriceandroid.model.Good;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * @author Developer
 */
public class ExportImport implements IExportImport {

    @Override
    public void exportData(String filePath, List<Good> goods) throws FileNotFoundException, UnsupportedEncodingException {
        TxtReaderWriter rw = new TxtReaderWriter(filePath);
        String forExport;
        forExport = CSV.makeCSVString(goods);
        rw.writeToFile(forExport);

    }

    @Override
    public List<Good> importData(String filePath) throws Exception {
        TxtReaderWriter rw = new TxtReaderWriter(filePath);
        String imported = rw.ReadFromFile();
        List<Good> goods;
        goods = CSV.makeListFromCSVString(imported);
        return goods;
    }

}
