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
package tdc2013.web;

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Scope;
import javax.persistence.EntityManager;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import tdc2013.hibernate.model.Pessoa;
import tdc2013.hibernate.model.PessoaRepository;
import tdc2013.hibernate.model.Sexo;
import tdc2013.web.interfaces.Calculos;
import tdc2013.web.interfaces.CustomScriptGroovy;
import tdc2013.web.interfaces.CustomScriptJavaScript;
import tdc2013.web.interfaces.CustomScriptPython;

@Named
@RequestScoped
public class PessoaService {

    @Inject
    PessoaRepository pessoaRepository;
    @Inject
    Calculos calculos;
    @Inject
    CustomScriptGroovy scriptGroovy;
    @Inject
    CustomScriptJavaScript scriptJavaScript;
    @Inject
    CustomScriptPython scriptPython;
    
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public Pessoa getResultFind1() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1l);
        pessoa.setNome("Mr. Jonnes");
        pessoa.setSexo(Sexo.MASCULINO);

        // em.merge(pessoa);

        //return pessoaRepository.findBySexoEqual(Sexo.MASCULINO);
        return pessoa;
    }

    public String opJS(int n1, int n2) {
        return op(scriptJavaScript.soma(n1, n2), scriptJavaScript.diminui(n1, n2), scriptJavaScript.divide(n1, n2), scriptJavaScript.multiplica(n1, n2), "javascript");
    }

    public String opGV(int n1, int n2) {
        return op(scriptGroovy.soma(n1, n2), scriptGroovy.diminui(n1, n2), scriptGroovy.divide(n1, n2), scriptGroovy.multiplica(n1, n2), "groovy");
    }
    
    public String opPY(int n1, int n2) {
        return op(scriptPython.soma(n1, n2), scriptPython.diminui(n1, n2), scriptPython.divide(n1, n2), scriptPython.multiplica(n1, n2),"python");
    }
    
    public String opJSClientServer(int n1, int n2) {
        return op(calculos.soma(n1, n2), calculos.diminui(n1, n2), calculos.divide(n1, n2), calculos.multiplica(n1, n2), "nashorn");
    }
    
    private String op(Number n1, Number n2, Number n3, Number n4, String engine){
        ScriptEngineFactory factory = new ScriptEngineManager().getEngineByName(engine).getFactory(); 
        StringBuilder builder = new StringBuilder();
        builder.append("engine name=").append(factory.getEngineName());
        builder.append("\nengine version=").append(factory.getEngineVersion());
        builder.append("\nlanguage name=").append(factory.getLanguageName());
        builder.append("\nextensions=").append(factory.getExtensions());
        builder.append("\nlanguage version=").append(factory.getLanguageVersion());
        builder.append("\nnames=").append(factory.getNames());
        builder.append("\nmime types=").append(factory.getMimeTypes());
        
        return String.format("soma = %s\nsubtração=%s\ndivisão=%s\nmultiplicação=%s\n\n%s", n1, n2, n3, n4, builder.toString());
    }
}
