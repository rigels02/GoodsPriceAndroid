
package org.rb.goodspriceandroid.reports;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Developer
 */
public class StringFormater {
    private final static String FMT= "%.2f";
    private final static String DSCR="Shop:Good:DeltaPrice:in %:DeltaDiscount: in %";
    
    private String printFmtStrPerDay(Map<Integer, DayReport> monthReport){
        StringBuilder report= new StringBuilder();
        Set<Integer> keys = monthReport.keySet();
         for (Integer key : keys) {
            report.append(key).append(":");
           List<ReportModel> perDay = monthReport.get(key).getPerDay();
             for (ReportModel model  : perDay) {
                 report.append("\t").append(model.shop).append(":").append(model.name)
                       .append(":")
                         .append(String.format(FMT, model.dprice))
                         .append(":")
                       .append(String.format(FMT, model.dpricePercent))
                         .append("% : ")
                       .append(String.format(FMT, model.ddiscount))
                         .append(":")
                         .append(String.format(FMT, model.ddiscountPercent))
                       .append("%\n"); //.append("\n\t");
                 
             }
         //report.append("\n");
                
        }
       
       return report.toString();
    }
    
    /**
     * Get formated string report for month report
     * @param monthYearReport
     * @return formated string
     */
    public String printFmtStr(MonthYearReport monthYearReport){
      StringBuilder report= new StringBuilder();
       report.append("for Month: ").append(ReportUtils.getMMname(monthYearReport.getMy().getMm()))
               .append(" Year:").append(ReportUtils.getYYname(monthYearReport.getMy().getYy()))
               .append("\n");
       report.append(DSCR).append("\n");
       report.append("==============================").append("\n");
        Map<Integer, DayReport> reportPerDay = monthYearReport.getReportsPerDay();
        String sPerDay = printFmtStrPerDay(reportPerDay);
        if(sPerDay.isEmpty())
            sPerDay="No data\n";
        report.append(sPerDay);
        report.append("-------------------------------").append("\n");
       return report.toString();
    }
    
    public String printFmtStr(List<MonthYearReport> monthYearReports){
      StringBuilder report= new StringBuilder();
    
        for (MonthYearReport monthYearReport : monthYearReports) {
            report.append(printFmtStr(monthYearReport));
        }
      return report.toString();
              
    }
    
    /*public static void main(String[] args) {
    int month= 12;
    System.out.println(new DateFormatSymbols().getMonths()[month-1]);
    System.out.println(new SimpleDateFormat("MMMM").format(new Date(1, month-1, 1)));
    System.out.println(new SimpleDateFormat("YYYY").format(new Date(116, 1, 1)));
    //System.out.println(new SimpleDateFormat("MMMM").format(new Date(1, month-1, 1)));
    }*/
}
