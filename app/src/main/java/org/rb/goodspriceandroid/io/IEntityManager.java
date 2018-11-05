
package org.rb.goodspriceandroid.io;

import java.util.List;

/**
 *
 * @author Developer
 * @param <T>
 */
public interface IEntityManager <T> {
    
    interface EntityTransaction{
        void begin();
        void commit();
    }
    
    void persist(Object entity);
    T merge(T entity);
    void remove(Object entity);
    EntityTransaction getTransaction();
    List<T> findAll();
}
