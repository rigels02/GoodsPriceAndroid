
package org.rb.goodspriceandroid.views;


import org.rb.goodspriceandroid.model.Good;

import java.util.List;

/**
 *
 * @author Developer
 */
public interface IListView {
    
    /**
     * Update the list view
     * @param goods 
     */
    void updateListView(List<Good> goods);
}
