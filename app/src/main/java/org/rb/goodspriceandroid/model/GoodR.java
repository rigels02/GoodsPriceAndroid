package org.rb.goodspriceandroid.model;

public class GoodR extends Good {

    private long id;

    public  GoodR(){
        super();
    }
    public GoodR(Good good) {
        super(good.getCdate(),good.getName(),good.getShop(),good.getPrice(),good.getDiscount());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public Good makeGood() throws CloneNotSupportedException {
       return (Good) super.clone();
        //return new Good(this.getCdate(),this.getName(),this.getShop(),this.getPrice(),this.getDiscount());
    }
}
