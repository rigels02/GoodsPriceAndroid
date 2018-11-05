package org.rb.goodspriceandroid.reports;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Developer
 */
public class HtmlFormater {

    private final static String FMT = "%.2f";
    private final static String DSCR = "Shop:Good:DeltaPrice:in %:DeltaDiscount: in %";

    private String printFmtStrPerDay_1(Map<Integer, DayReport> monthReport) {
        StringBuilder report = new StringBuilder();
        Set<Integer> keys = monthReport.keySet();

        for (Integer key : keys) {
            report.append("<tr>");
            report.append("<td>").append(key).append(":").append("</td>");
            List<ReportModel> perDay = monthReport.get(key).getPerDay();

            int i = 0;
            for (ReportModel model : perDay) {
                report.append((i == 0) ? "" : "<tr><td></td>").append("<td>").append(model.shop).append(":").append(model.name)
                        .append(": ")
                        .append(String.format(FMT, model.dprice))
                        .append(":")
                        .append(String.format(FMT, model.dpricePercent))
                        .append("% : ")
                        .append(String.format(FMT, model.ddiscount))
                        .append(":")
                        .append(String.format(FMT, model.ddiscountPercent))
                        .append("%"); //.append("\n\t");
                i++;
                report.append("</td>").append("</tr>");
            }

            //report.append("</tr>");
        }

        return report.toString();
    }

    private String printFmtStrPerDay(Map<Integer, DayReport> monthReport) {
        StringBuilder report = new StringBuilder();
        Set<Integer> keys = monthReport.keySet();
        report.append("<pre>");
        for (Integer key : keys) {
            report.append(key).append(":");
            List<ReportModel> perDay = monthReport.get(key).getPerDay();
            int i = 0;
            for (ReportModel model : perDay) {
                report.append((i == 0) ? "&emsp;" : "&emsp;&emsp;").append(model.shop).append(":").append(model.name)
                        .append(": ")
                        .append(String.format(FMT, model.dprice))
                        .append(":")
                        .append(String.format(FMT, model.dpricePercent))
                        .append("% : ")
                        .append(String.format(FMT, model.ddiscount))
                        .append(":")
                        .append(String.format(FMT, model.ddiscountPercent))
                        .append("%<br>"); //.append("\n\t");
                i++;
            }
            report.append("</pre>");

        }

        return report.toString();
    }

    /**
     * Get formated string report for month report
     *
     * @param monthYearReport
     * @return formated string
     */
    public String printFmtStr_1(MonthYearReport monthYearReport) {
        StringBuilder report = new StringBuilder();
        report.append("<span style='color:blue'> &rArr; Month: ").append(ReportUtils.getMMname(monthYearReport.getMy().getMm()))
                .append(" Year:").append(ReportUtils.getYYname(monthYearReport.getMy().getYy()))
                .append("</span><br>");
        report.append("<span style='color:red'>").append(DSCR).append("</span><br>");
        report.append("<hr>");
        Map<Integer, DayReport> reportPerDay = monthYearReport.getReportsPerDay();
        String sPerDay = printFmtStrPerDay_1(reportPerDay);
        if (sPerDay.isEmpty()) {
            sPerDay = "No data<br>";
        }
        report.append("<table>").append(sPerDay).append("</table>");
        report.append("<hr>").append("<br>");
        return report.toString();
    }

    /**
     * Get formated string report for month report
     *
     * @param monthYearReport
     * @return formated string
     */
    public String printFmtStr(MonthYearReport monthYearReport) {
        StringBuilder report = new StringBuilder();
        report.append("<span style='color:blue'> &rArr; Month: ").append(ReportUtils.getMMname(monthYearReport.getMy().getMm()))
                .append(" Year:").append(ReportUtils.getYYname(monthYearReport.getMy().getYy()))
                .append("</span><br>");
        report.append("<span style='color:red'>").append(DSCR).append("</span><br>");
        report.append("<hr>");
        Map<Integer, DayReport> reportPerDay = monthYearReport.getReportsPerDay();
        String sPerDay = printFmtStrPerDay(reportPerDay);
        if (sPerDay.isEmpty()) {
            sPerDay = "No data<br>";
        }
        report.append("<b>").append(sPerDay).append("</b>");
        report.append("<hr>").append("<br>");
        return report.toString();
    }

    public String printFmtStr(List<MonthYearReport> monthYearReports) {
        StringBuilder report = new StringBuilder();

        for (MonthYearReport monthYearReport : monthYearReports) {
            report.append(printFmtStr(monthYearReport));
        }
        return report.toString();

    }
    
    public String printFmtStr_1(List<MonthYearReport> monthYearReports) {
        StringBuilder report = new StringBuilder();

        for (MonthYearReport monthYearReport : monthYearReports) {
            report.append(printFmtStr_1(monthYearReport));
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
