
package org.rb.goodspriceandroid.io;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class JpaRepositoryImpl<T> implements IJpaRepository<T> {

    private IEntityManager<T> em;

    public JpaRepositoryImpl(IEntityManager<T> em) {
        this.em = em;
    }
    
    


    @Override
    public void saveList(List<T> ListOfObjects) {
     if (em != null) {
            em.getTransaction().begin();
            for (T ListOfObject : ListOfObjects) {
                em.persist(ListOfObject);
            }
            em.getTransaction().commit();
        }    
    }

    @Override
    public List<T> readListDB() {
    List<T> ListOfObjects=null;
        if (em != null) {
            em.getTransaction().begin();
           
            ListOfObjects= em.findAll();
            em.getTransaction().commit();
        }

        
        return ListOfObjects;  
    }

    @Override
    public Date getTimeStamp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
