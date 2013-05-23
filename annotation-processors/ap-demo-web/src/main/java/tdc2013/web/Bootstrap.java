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

import java.util.Scanner;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import tdc2013.hibernate.model.EstadoCivil;
import tdc2013.hibernate.model.Pessoa;
import tdc2013.hibernate.model.PessoaRepository;
import tdc2013.hibernate.model.Script;
import tdc2013.hibernate.model.ScriptRepository;
import tdc2013.hibernate.model.Sexo;

@Singleton
@Startup
public class Bootstrap {

    @Inject
    EntityManager em;
    @Inject
    ScriptRepository scriptRepository;
    @Inject
    PessoaRepository pessoaRepository;

    @PostConstruct
    public void init() {
        String codeGV = new Scanner(getClass().getResourceAsStream("/script.groovy")).useDelimiter("\\Z").next();
        String codeJS = new Scanner(getClass().getResourceAsStream("/script.js")).useDelimiter("\\Z").next();
        String codePY = new Scanner(getClass().getResourceAsStream("/script.py")).useDelimiter("\\Z").next();
        String codeJSRepository = new Scanner(getClass().getResourceAsStream("/scriptRepository.js")).
                useDelimiter("\\Z").next();

        Script scriptGV = em.find(Script.class, 1L);
        Script scriptJS = em.find(Script.class, 2L);
        Script scriptPY = em.find(Script.class, 3L);
        Script scriptJSRepository = em.find(Script.class, 4L);

        if (scriptGV == null) {
            scriptGV = new Script();
            scriptGV.setId(1L);
            scriptGV.setType("groovy");
        }
        scriptGV.setCode(codeGV);

        if (scriptJS == null) {
            scriptJS = new Script();
            scriptJS.setId(2L);
            scriptJS.setType("javaScript");
        }
        scriptJS.setCode(codeJS);

        if (scriptPY == null) {
            scriptPY = new Script();
            scriptPY.setId(3L);
            scriptPY.setType("python");
        }
        scriptPY.setCode(codePY);

        if (scriptJSRepository == null) {
            scriptJSRepository = new Script();
            scriptJSRepository.setId(4L);
            scriptJSRepository.setType("js");
        }
        scriptJSRepository.setCode(codeJSRepository);

        scriptRepository.save(scriptGV);
        scriptRepository.save(scriptJS);
        scriptRepository.save(scriptPY);
        scriptRepository.save(scriptJSRepository);

        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1L);
        pessoa1.setNome("Carlos da Silva");
        pessoa1.setEstadoCivil(EstadoCivil.CASADO);
        pessoa1.setSexo(Sexo.MASCULINO);

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2L);
        pessoa2.setNome("Mario de Andrade");
        pessoa2.setEstadoCivil(EstadoCivil.SOLTEIRO);
        pessoa2.setSexo(Sexo.MASCULINO);

        Pessoa pessoa3 = new Pessoa();
        pessoa3.setId(3L);
        pessoa3.setNome("Cíntia de Souza");
        pessoa3.setEstadoCivil(EstadoCivil.SOLTEIRO);
        pessoa3.setSexo(Sexo.FEMININO);

        Pessoa pessoa4 = new Pessoa();
        pessoa4.setId(4L);
        pessoa4.setNome("Maria Bernadete");
        pessoa4.setEstadoCivil(EstadoCivil.SOLTEIRO);
        pessoa4.setSexo(Sexo.FEMININO);

        pessoaRepository.save(pessoa1);
        pessoaRepository.save(pessoa2);
        pessoaRepository.save(pessoa3);
        pessoaRepository.save(pessoa4);

    }
}
