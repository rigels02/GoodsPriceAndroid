
package org.rb.goodspriceandroid.reports;

/**
 *
 * @author Developer
 */
class MonthYear {
    private int mm;
    private int yy;

    MonthYear(int mm, int yy) {
        this.mm = mm;
        this.yy = yy;
    }

     int getMm() {
        return mm;
    }

     void setMm(int mm) {
        this.mm = mm;
    }

    int getYy() {
        return yy;
    }

    void setYy(int yy) {
        this.yy = yy;
    }

    @Override
     public String toString() {
        return "{" + "mm=" + mm + ", yy=" + yy + '}';
    }
    
    
    
}
