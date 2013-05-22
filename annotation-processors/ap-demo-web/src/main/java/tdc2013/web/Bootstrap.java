/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.web;

import java.util.Scanner;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import tdc2013.hibernate.model.Script;
import tdc2013.hibernate.model.ScriptRepository;


/**
 * Web application lifecycle listener.
 *
 * @author Klaus Boeing
 */
@Named
@WebListener()
public class Bootstrap implements ServletContextListener {
    @Inject
    EntityManager em;
    
    @Inject ScriptRepository repository;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        String codeGV = new Scanner(getClass().getResourceAsStream("/script.groovy")).useDelimiter("\\Z").next();
        String codeJS = new Scanner(getClass().getResourceAsStream("/script.js")).useDelimiter("\\Z").next();
        String codePY = new Scanner(getClass().getResourceAsStream("/script.py")).useDelimiter("\\Z").next();
        
        Script scriptGV = em.find(Script.class, 1L);
        Script scriptJS = em.find(Script.class, 2L);
        Script scriptPY = em.find(Script.class, 3L);
        
        if(scriptGV == null){
            scriptGV = new Script();
            scriptGV.setId(1L);
            scriptGV.setCode(codeGV);
        }
        
        if(scriptJS == null){
            scriptJS = new Script();
            scriptJS.setId(2L);
            scriptJS.setCode(codeJS);
        }
        
        if(scriptPY == null){
            scriptPY = new Script();
            scriptPY.setId(3L);
            scriptPY.setCode(codePY);
        }
        
        repository.save(scriptGV);
        repository.save(scriptJS);
        repository.save(scriptPY);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       
    }
}
