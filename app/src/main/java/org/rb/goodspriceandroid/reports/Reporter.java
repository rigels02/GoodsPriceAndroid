
package org.rb.goodspriceandroid.reports;




import org.rb.goodspriceandroid.model.Good;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Developer
 */
 public class Reporter {
    
    private final Map<String,Good> baseList= new HashMap<>();
    private Date startDate;
    private final List<Good> timeList= new ArrayList<>();
    private final List<Good> allList;
    
    private boolean singleShopSelected;
    
    
    
    
    public Reporter(List<Good> allList)  {
        this.singleShopSelected = false;
        this.allList = allList;
        if(allList==null)
            try {
                throw new IllegalAccessException("allList==null not allowed!");
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Reporter.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        
    }
    
    /**
     * Get the first occurence of good by name for selected shop and put into
     * category map with key= good.name if its not already there.
     * @param startDate consider only elements started with startDate and later
     * @param shop  select only elements for selected shop
     */
    void initData(Date startDate, String shop) {
        this.startDate= startDate;
        if(startDate==null)
            try {
                throw new IllegalAccessException("startDate==null, not allowed!");
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Reporter.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if(baseList.size()>0)
            baseList.clear();
        if(shop == null || shop.isEmpty()){
            singleShopSelected=false;
            buildBaseList(startDate);
           
        }else{
            singleShopSelected=true;
            buildBaseList(startDate, shop);
            
        }
        makeTimeList(shop);
        
    }
    /**
     * Build BaseList with selected shop.
     * The base list contains items in startDate used as base for diferences 
     * calculation
     * @param startDate
     * @param shop selected shop
     */
    private void buildBaseList( Date startDate, String shop){
        for (Good good : allList) {
        if( (!baseList.containsKey(good.getName())) 
                    && good.getShop().contains(shop)
                    && good.getCdate().compareTo(startDate)>= 0
                    ){
                alignPriceValue(good);
                baseList.put(good.getName(), good);
            }
        }
    }
    
    /**
     * Build BaseList for all shops
     * The base lists keys are good.name+good.shop .
     * The base list contains items in startDate used as base for diferences 
     * calculation
     * @param startDate 
     */
    private void buildBaseList( Date startDate){
        for (Good good : allList) {
        if( (!baseList.containsKey(good.getName()+good.getShop())) 
                   
                    && good.getCdate().compareTo(startDate)>= 0
                    ){
                alignPriceValue(good);
                baseList.put(good.getName()+good.getShop(), good);
            }
        }
    }
    
    private void alignPriceValue(Good good) {
        if (good.getDiscount() == 0) {
            good.setDiscount(good.getPrice());
        } else if (good.getPrice() == 0) {
            good.setPrice(good.getDiscount());
        }
    }
    
    private void makeTimeList(String shop) {
        
        if(baseList.isEmpty())
             return;
        if(shop == null || shop.isEmpty()){
            buildTimeList();
        }else {
            buildTimeList(shop);
        }
        
    }
    /**
     * Build timeList for selected Shop
     * @param shop selected shop
     */
    private void buildTimeList(String shop) {
        
        for (Good good : allList) {
          if(good.getCdate().compareTo(startDate)>=0 
                  && good.getShop().contains(shop)){
            timeList.add(good);
          }
        }
    }
    /**
     *  Build timeList for all Shops
     */
    private void buildTimeList(){
        
        for (Good good : allList) {
          if(good.getCdate().compareTo(startDate)>=0 ){
            timeList.add(good);
          }
        }
    }
    /**
     * Generate DayReport map where key = date
     * @param month month for which to generate DayReports
     * @param year year for which to generate DayReports
     * @return map[day,DayReport]
     */
      Map<Integer, DayReport> monthReport(int month, int year){
        Map<Integer, DayReport> report= new HashMap<>();
        if(timeList.isEmpty())
            return report;
        for (Good good : timeList) {
            if((ReportUtils.getMonthPart(good.getCdate()) != month) ||
                    (ReportUtils.getYearPart(good.getCdate()) != year ))
               continue;
           int day= ReportUtils.getDatePart(good.getCdate());
           DayReport dr;
           //day is key
           if(report.containsKey(day)){
              dr = report.get(day);
              
           }else {
               dr = new DayReport();
               //dr.day=day;
           }
           if(singleShopSelected){
           dr.getPerDay().add(getReportModel(good, 
                          baseList.get(good.getName()))
               );
           }else {
             dr.getPerDay().add(getReportModel(good, 
                          baseList.get(good.getName()+good.getShop()))
               );
           }
           report.put(day, dr);
        }
        //Sort by keys (days)
          //return report;
        return new TreeMap<>(report);
    }

     /**
      * Generate MonthYearReport list for time between startDate and 
      * endDate 
      * @param startDate start Date
      * @param endDate end Date
      * @return list
      */
    List<MonthYearReport> timeReport(Date startDate, Date endDate){
        if(startDate.after(endDate)){
           throw new IllegalArgumentException("startDate > endDate - not allowed!");
         
        }
            
        ArrayList<MonthYearReport> report = new ArrayList<>();
        List<MonthYear> monthYears = ReportUtils.makeMonthYearList(startDate, endDate);
        for (MonthYear monthYear : monthYears) {
            Map<Integer, DayReport> monthReport = monthReport(monthYear.getMm(), monthYear.getYy());
            MonthYearReport monthYearReport = new MonthYearReport(monthYear, monthReport);
            report.add(monthYearReport);
        }
        return report;
    }
    
    /**
     * Get MonthYearReport for selected month and year
     * @param mm selected month
     * @param yy selected year
     * @param monthYearReports monthYearReports  list
     * @return monthYearReport or null if no record for selected mm, yy
     */
    MonthYearReport monthReport(int mm, int yy, List<MonthYearReport> monthYearReports){
        for (MonthYearReport monthYearReport : monthYearReports) {
            if(mm == monthYearReport.getMy().getMm() && yy == monthYearReport.getMy().getYy()){
              return monthYearReport;
            }
        }
        return null;
    }
    
    private ReportModel getReportModel(Good good, Good baseGood) {
        ReportModel model = new ReportModel();
        model.d= ReportUtils.getDatePart(good.getCdate());
        model.m= ReportUtils.getMonthPart(good.getCdate());
        model.y= ReportUtils.getYearPart(good.getCdate());
        model.name= good.getName();
        model.shop= good.getShop();
        alignPriceValue(good);
        model.dprice= good.getPrice()- baseGood.getPrice();
        model.dpricePercent= model.dprice/baseGood.getPrice()*100;
        model.ddiscount= good.getDiscount()- baseGood.getDiscount();
        model.ddiscountPercent=model.ddiscount/baseGood.getDiscount()*100;
        return model;
    }
    
    public List<MonthYearReport> getTimeReportForAllShops(Date startDate, Date endDate){
        initData(startDate, null);
        return timeReport(startDate, endDate);
    }

   
}
