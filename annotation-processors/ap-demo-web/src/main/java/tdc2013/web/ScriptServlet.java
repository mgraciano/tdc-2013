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

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tdc2013.hibernate.model.PessoaRepository;
import tdc2013.hibernate.model.Script;
import tdc2013.hibernate.model.ScriptRepository;
import tdc2013.web.script.PessoaRespositoryTester;
import tdc2013.web.script.PessoaRespositoryTesterScriptsProvider;

/**
 *
 * @author Klaus Boeing
 */
@Named
@WebServlet(name = "ScriptServlet", urlPatterns = {"/ScriptServlet"})
public class ScriptServlet extends HttpServlet {

    @Inject
    ScriptRepository repository;
    @Inject
    PessoaRepository pessoaRepository;
    @Inject
    TestScriptService scriptService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String scriptType = request.getParameter("script");
        Script script = repository.findByType(scriptType);

        response.setContentType("text/plain");
        response.getWriter().print(script.getCode());
        response.flushBuffer();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String type = request.getParameter("script");
            String result = "";

            if (type.equals("groovy")) {
                result = scriptService.opGV(10, 5);
            } else if (type.equals("javaScript")) {
                result = scriptService.opJS(10, 5);
            } else if (type.equals("python")) {
                result = scriptService.opPY(10, 5);
            } else if (type.equals("js")) {
                PessoaRespositoryTester pessoasRepositoryTester = new PessoaRespositoryTesterScriptsProvider().getScript();
                result = pessoasRepositoryTester.testPessoaRepository(pessoaRepository);
            }

            response.setContentType("text/plain");
            response.getWriter().print(result);
            response.flushBuffer();
        } catch (ScriptException ex) {
            Logger.getLogger(ScriptServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String scriptType = request.getParameter("script");
        String code = new Scanner(request.getInputStream()).useDelimiter("\\Z").next();

        Script script = repository.findByType(scriptType);
        script.setCode(code);

        repository.save(script);
    }
}
