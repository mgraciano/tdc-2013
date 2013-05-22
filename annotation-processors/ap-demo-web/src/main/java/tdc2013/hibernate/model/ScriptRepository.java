/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.hibernate.model;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Klaus Boeing
 */
@Stateless
public class ScriptRepository {
    
    @Inject EntityManager em;
    
    public void save(Script script){
        em.merge(script);
    }
    
    public Script findById(Long id){
        return em.find(Script.class, id);
    }
}
