/*
 * Copyright (c) 2013, Klaus Boeing & Michel Graciano.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the project's authors nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
