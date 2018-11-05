package org.rb.goodspriceandroid;

import org.junit.Test;
import org.rb.goodspriceandroid.io.TxtReaderWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TxtReaderWriterUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testReadTxtFile() throws Exception {
        TxtReaderWriter txtRW = new TxtReaderWriter("TestFile.txt");
        String bigStr= "Line of text 1\nLine of text 2\nLine of text 3\n\n";
        txtRW.writeToFile(bigStr);
        String result = txtRW.ReadFromFile();
        assertEquals("The same",bigStr,result);


    }
    @Test
    public  void testReadTxtFileAsList() throws IOException {
        TxtReaderWriter txtRW = new TxtReaderWriter("TestFile.txt");
        String bigStr= "Line of text 1\nLine of text 2\nLine of text 3\n\n";
        List<String> expected = Arrays.asList(bigStr.split("\n"));
        txtRW.writeToFile(bigStr);
        List<String> lst = txtRW.ReadFromFileAsStrList();
        assertEquals("SHould be as expected",expected,lst);
    }
}