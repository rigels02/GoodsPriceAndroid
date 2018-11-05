
package org.rb.goodspriceandroid.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Developer
 */
public class TxtReaderWriter {
    
    private String filePath;
    private PrintStream out;
    

    public TxtReaderWriter(String filePath) {
        this.filePath = filePath;
    }
    
    private void openWriter() throws FileNotFoundException, UnsupportedEncodingException {
       
            out = new PrintStream(filePath, "UTF-8");
            
    }
    public void writeToFile(String txt) throws FileNotFoundException, UnsupportedEncodingException{
        openWriter();
        out.print(txt);
        out.close();
    }
    public String ReadFromFile() throws IOException{

        //Path path = Paths.get(filePath);
         File ffile= new File(filePath);

        if(ffile.length()>= Integer.MAX_VALUE){
           throw new IOException("MAX size of text file reached!");
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(ffile));
        String line;
        while((line = br.readLine()) != null){
            sb.append(line).append("\n");
        }

        br.close();
        return sb.toString();
    }
    
    public List<String> ReadFromFileAsStrList() throws IOException{

        String bigString = ReadFromFile();
        List<String> lst;
        String[] sarr = bigString.split("\n");
        lst = Arrays.asList(sarr);
        return lst;
    }
}
