
package org.rb.goodspriceandroid.reports;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Developer
 */
class DayReport {
    
    //int day;
    private List<ReportModel> perDay;
    

    public DayReport() {
       
        perDay = new ArrayList<>();
        //day=0;
    }

   List<ReportModel> getPerDay() {
        return perDay;
    }

    @Override
    public String toString() {
        return "DayReport{" + "\nperDay=" + perDay + "\n}";
    }
    
}
