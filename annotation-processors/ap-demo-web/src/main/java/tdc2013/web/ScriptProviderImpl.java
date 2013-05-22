/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import tdc2013.hibernate.model.Script;
import tdc2013.hibernate.model.ScriptRepository;
import tdc2013.script.ScriptProvider;

/**
 *
 * @author Klaus Boeing
 */
public class ScriptProviderImpl implements ScriptProvider {

    private Long scriptId;

    public ScriptProviderImpl(Long scriptId) {
        this.scriptId = scriptId;
    }

    @Override
    public String getScript() {
        return getRepository().findById(scriptId).getCode();
    }

    public ScriptRepository getRepository() {
        try {
            InitialContext initialContext = new InitialContext();
            return (ScriptRepository) initialContext.lookup("java:global/tdc-2013_annotation-processors-demo-web_war_1.0-SNAPSHOT/ScriptRepository!tdc2013.hibernate.model.ScriptRepository");
        } catch (NamingException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Couldn't get BeanManager through JNDI", e);
        }
        return null;
    }

    public static final class Groovy extends ScriptProviderImpl {

        public Groovy() {
            super(1L);
        }
    }

    public static final class JavaScript extends ScriptProviderImpl {

        public JavaScript() {
            super(2L);
        }
    }

    public static final class Python extends ScriptProviderImpl {

        public Python() {
            super(3L);
        }
    }
}