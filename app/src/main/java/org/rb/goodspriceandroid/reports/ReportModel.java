
package org.rb.goodspriceandroid.reports;

/**
 * <pre>
 * int d,m,y; //day,month,year
 *   String name; //good name
 *   double dprice; //delta price related to first bought good after startTime
 *   double ddiscount; //delta discount related to first bought good after startTime
 *   String shop; //shop where the good has been bought
 * @author Developer
 * </pre>
 */
 class ReportModel {
    
    int d,m,y; //day,month,year
    String name; //good name
    double dprice; //delta price related to first bought good after startTime
    double dpricePercent;//delta price in percents
    double ddiscount; //delta discount related to first bought good after startTime
    double ddiscountPercent; //delta discount in percents
    String shop; //shop where the good has been bought

     @Override
     public String toString() {
     return "\n>d=" + "d=" + d + ", m=" + m + ", y=" + y + ", name=" + name + ", dprice=" + dprice + ", dpricePercent=" + dpricePercent + ", ddiscount=" + ddiscount + ", ddiscountPercent=" + ddiscountPercent + ", shop=" + shop;
    
     }

     
}
