
package org.rb.goodspriceandroid.reports;

import java.util.Map;

/**
 *
 * @author Developer
 */
public class MonthYearReport {
    
    private MonthYear my;
    //key = Day ,for every day in month we have DayReport
    private Map<Integer, DayReport> reportsPerDay;

    /**
     * Build report for every MonthYear. Every month has a map of reports per day
     * @param my MonthYear having the map
     * @param reportPerDay map of DayReport's 
     */
    MonthYearReport(MonthYear my, Map<Integer, DayReport> reportPerDay) {
        this.my = my;
        this.reportsPerDay = reportPerDay;
    }

    MonthYear getMy() {
        return my;
    }

    Map<Integer, DayReport> getReportsPerDay() {
        return reportsPerDay;
    }

    @Override
    public String toString() {
        return "\nMonthYearReport{" + "my=" + my + ",\nreportsPerDay=\n" + reportsPerDay + '}';
    }
    
    
}
