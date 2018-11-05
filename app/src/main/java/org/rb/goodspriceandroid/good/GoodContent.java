package org.rb.goodspriceandroid.good;

import org.rb.goodspriceandroid.model.Good;


import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 *
 */
public class GoodContent {

    /**
     * An array of  (Good) items.
     */
    public static final List<GoodItem> ITEMS = new ArrayList<GoodItem>();

    private static List<String> namesList;
    private static List<String> shopsList;
    /**
     * A map of  (Good) items, by ID. Seems, not going to be used
     */
    //public static final Map<String, GoodItem> ITEM_MAP = new HashMap<String, GoodItem>();

    //---------For test -----------------//
  /*
    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createGoodItem(i,new Good(new Date(117,1,i),"name_"+i,"shop_"+i,12.12,0.0)));
        }
    }
    */
    //---------end For test -----------------//

    private static void addItem(GoodItem item){
        ITEMS.add(item);
        //ITEM_MAP.put(item.id,item);
    }


    public static Good getGood(int idx){
        return ITEMS.get(idx).details;
    }

    public static void populateGoodItems(List<Good> goods){
        ITEMS.clear();
        for (int i=0; i< goods.size();i++) {
            addItem(createGoodItem(i+1,goods.get(i)));

        }
        namesList= buildNamesList(goods);
        shopsList= buildShopsList(goods);

    }
    private static GoodItem createGoodItem(int position, Good Good) {
        //return new GoodItem(String.valueOf(position), "Item " + position, makeDetails(position));
        return new GoodItem(String.valueOf(position), Good.toString(), Good);

    }

    /**
     * Build names list from GoodContent.
     * GoodContent must keep info about Good's Name and Shop fields
     * @return
     */
    private static List<String> buildNamesList(List<Good> goods){
        List<String> names = new ArrayList<>();

        for(Good item: goods) {
            if( ! names.contains(item.getName())){
                names.add(item.getName());
            }
        }
        return names;
    }

    public static List<String> getNamesList() {
        return namesList;
    }

    public static List<String> getShopsList() {
        return shopsList;
    }

    /**
     * Build Shops list from GoodContent.
     * GoodContent must keep info about Good's Name and Shop fields
     * @return
     */
    private static List<String> buildShopsList(List<Good> goods){
        List<String> shops = new ArrayList<>();
        for(Good item: goods) {
            if( ! shops.contains(item.getShop())){
                shops.add(item.getShop());
            }
        }
        return shops;
    }

    /**
     * A Good item representing a piece of content.
     */
     public static class GoodItem {
        public  String id;
        public  String content;

        // if model bean (here Good) is not big we can store it completely in details field.
        //
        public  Good details;


        public GoodItem(String id, String content, Good details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
