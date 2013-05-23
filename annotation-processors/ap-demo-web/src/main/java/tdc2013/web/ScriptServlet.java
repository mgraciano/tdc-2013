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

import java.io.IOException;
import java.util.Scanner;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tdc2013.hibernate.model.PessoaRepository;
import tdc2013.hibernate.model.Script;
import tdc2013.hibernate.model.ScriptRepository;

@Named
@WebServlet(name = "ScriptServlet", urlPatterns = {"/ScriptServlet"})
public class ScriptServlet extends HttpServlet {

    @Inject
    ScriptRepository repository;
    @Inject
    PessoaRepository pessoaRepository;
    @Inject
    FolhaPagamentoService folhaPagamentoService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String engine = request.getParameter("engine");
        String name = request.getParameter("name");
        Script script = repository.findByNameEqualAndTypeEqual(name, engine);

        response.setContentType("text/plain");
        response.getWriter().print(script.getCode());
        response.flushBuffer();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String engine = request.getParameter("engine");
        String name = request.getParameter("name");
        String result = "";
        
        switch (name) {
            case "folhaPagamento":
                result = folhaPagamentoService.getResumoCalculoFolha(engine, 1780.23, 28, 2.4);
                break;
        }

        response.setContentType("text/plain");
        response.getWriter().print(result);
        response.flushBuffer();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String engine = request.getParameter("engine");
        String name = request.getParameter("name");
        String code = new Scanner(request.getInputStream()).useDelimiter("\\Z").next();

        Script script = repository.findByNameEqualAndTypeEqual(name, engine);
        script.setCode(code);

        repository.save(script);
    }
}
