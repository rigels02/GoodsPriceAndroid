package org.rb.goodspriceandroid.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Developer
 */
public class ReportUtils {

    static final private String DFMT = "dd-MM-yyyy";

   public static String date2String(Date date, String format) {
        if (format == null || format.isEmpty()) {
            format = DFMT;
        }
        String sDate;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sDate = sdf.format(date);
        return sDate;
    }

    public static int getDatePart(Date cdate) {
        return cdate.getDate();
    }

    public static int getMonthPart(Date cdate) {
        return cdate.getMonth();
    }

    public static int getYearPart(Date cdate) {
        return cdate.getYear();
    }
    
    static String getDDStr(int dd) {
        if(dd<0 || dd>31)
            return null;
        
        return String.valueOf(dd);
       
    }

    static String getMMStr(int mm) {
        if(mm<0 || mm> 11)
            return null;
       
        return String.valueOf(mm);
    }

    static String getYYStr(int yyyy) {
        if(yyyy< 0)
            return null;
        return String.valueOf(yyyy);
    }

    static String getMMname(int mm){
        //Locale.getDefault() will give Latvian for Latvia
       return new SimpleDateFormat("MM",Locale.ENGLISH).format(new Date(1, mm, 1));
    }
    static String getYYname(int yy){
       return new SimpleDateFormat("yyyy").format(new Date(yy, 1, 1));
    }
    static List<MonthYear> makeMonthYearList(Date startDate, Date endDate){
        List<MonthYear> mY = new ArrayList<>();
        
        int sm= ReportUtils.getMonthPart(startDate);
        int sy= ReportUtils.getYearPart(startDate);
        int em= ReportUtils.getMonthPart(endDate);
        int ey= ReportUtils.getYearPart(endDate);
                
        for( int y= sy; y<= ey; y++){
            if(sy == ey){               //in one year/ the same year
               theSameYear(y,sm,sy,em,ey, mY);
            }else if(y < ey && y == sy){ // The first year
                theFirstYear(y,sm,sy,em,ey,mY);
            }else if( y < ey ){          //Midle year
               theMidleYear(y,sm,sy,em,ey, mY);
            }else {                     //The Last year
              theLastYear(y,sm,sy,em,ey, mY);
            } 
        }
        return mY;
    }

    private static void theSameYear(int y, int sm, int sy, int em, int ey, List<MonthYear> mY) {
        for(int m= sm ; m<= em; m++){
            MonthYear my = new MonthYear(m, y);
            mY.add(my);
        }
    }

    private static void theFirstYear(int y,int sm, int sy, int em, int ey, List<MonthYear> mY) {
        for(int m=sm; m<= 11;m++){
         MonthYear my = new MonthYear(m, y);
            mY.add(my);
        }
    }

    private static void theMidleYear(int y,int sm, int sy, int em, int ey, List<MonthYear> mY) {
        for(int m=0; m<= 11;m++){
         MonthYear my = new MonthYear(m, y);
            mY.add(my);
        }
    }

    private static void theLastYear(int y,int sm, int sy, int em, int ey, List<MonthYear> mY) {
        for(int m=0; m<= em;m++){
         MonthYear my = new MonthYear(m, y);
            mY.add(my);
        }
    }
}
