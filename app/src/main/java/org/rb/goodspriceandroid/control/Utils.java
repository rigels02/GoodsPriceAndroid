
package org.rb.goodspriceandroid.control;




import org.rb.goodspriceandroid.model.Good;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Developer
 */
class Utils {
    
    static List<String> getGoodsNameList(List<Good> goods){
        List<String> list= new ArrayList<>();
        for (Good good: goods ) {
            if( !list.contains(good.getName())){
                list.add(good.getName());
            }
        }

        return list;
    }
    
    static List<String> getShopNameList(List<Good> goods){
        List<String> list= new ArrayList<>();
        for (Good good : goods) {
            if( !list.contains(good.getShop())){
              list.add(good.getShop());
            }
        }
        return list;
    }
}
