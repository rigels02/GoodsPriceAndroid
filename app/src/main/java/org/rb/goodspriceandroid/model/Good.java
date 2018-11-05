
package org.rb.goodspriceandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Developer
 */
public class Good implements Serializable , Cloneable, Parcelable {
    
    private static final long serialVersionUID = 1L;
    
   public static final int FCOUNT = 5;
    private Date   cdate;
    private String name;
    private String shop;
    private double price;
    private double discount;
   

    public Good(){
        cdate = new Date();
        name="";shop="";price=0;discount=0;
    }
    public Good(Date cdate, String name, String shop, double price, double discount) {
        this.cdate = cdate;
        this.name = name;
        this.shop = shop;
        this.price = price;
        this.discount = discount;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }
    public Good makeCopy() throws CloneNotSupportedException{
     return (Good) this.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Good good = (Good) o;

        if (Double.compare(good.price, price) != 0) return false;
        if (Double.compare(good.discount, discount) != 0) return false;
        if (cdate != null ? !cdate.equals(good.cdate) : good.cdate != null) return false;
        if (name != null ? !name.equals(good.name) : good.name != null) return false;
        return shop != null ? shop.equals(good.shop) : good.shop == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = cdate != null ? cdate.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (shop != null ? shop.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(discount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    
    @Override
    public String toString() {
        return "" + dformat() + ", name=" + name + ", shop=" + shop + ", price=" + price + ", discount=" + discount;
    }

   private String dformat(){
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");	
                
               return sdf.format(cdate);
               
   }
   
    public static Good getFromStream(DataInputStream din) throws IOException {
        
        Good good = new Good();
        //long tt = new Date().getTime();
        Date d = new Date();
        d.setTime(din.readLong());
        good.setCdate(d);
        good.setName(din.readUTF());
        good.setShop(din.readUTF());
        good.setPrice(din.readDouble());
        good.setDiscount(din.readDouble());
        return good;
    }
    
    public static void putToStream(DataOutputStream dos, Good good) throws IOException{
     dos.writeLong(good.getCdate().getTime());
     dos.writeUTF(good.getName());
     dos.writeUTF(good.getShop());
     dos.writeDouble(good.getPrice());
     dos.writeDouble(good.getDiscount());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.cdate != null ? this.cdate.getTime() : -1);
        dest.writeString(this.name);
        dest.writeString(this.shop);
        dest.writeDouble(this.price);
        dest.writeDouble(this.discount);
    }

    protected Good(Parcel in) {
        long tmpCdate = in.readLong();
        this.cdate = tmpCdate == -1 ? null : new Date(tmpCdate);
        this.name = in.readString();
        this.shop = in.readString();
        this.price = in.readDouble();
        this.discount = in.readDouble();
    }

    public static final Creator<Good> CREATOR = new Creator<Good>() {
        @Override
        public Good createFromParcel(Parcel source) {
            return new Good(source);
        }

        @Override
        public Good[] newArray(int size) {
            return new Good[size];
        }
    };
}
