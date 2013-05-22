/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tdc2013.hibernate.model.Script;
import tdc2013.hibernate.model.ScriptRepository;

/**
 *
 * @author Klaus Boeing
 */
@Named
@WebServlet(name = "ScriptServlet", urlPatterns = {"/ScriptServlet"})
public class ScriptServlet extends HttpServlet {

    @Inject
    ScriptRepository repository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String scriptType = request.getParameter("script");
        Script script = null;
        switch (scriptType) {
            case "groovy":
                script = repository.findById(1L);
                break;
            case "javaScript":
                script = repository.findById(2L);
                break;
            case "python":
                script = repository.findById(3L);
                break;
        }

        response.setContentType("text/plain");
        response.getWriter().print(script.getCode());
        response.flushBuffer();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String scriptType = request.getParameter("script");
        String code = new Scanner(request.getInputStream()).useDelimiter("\\Z").next();
        Script script = null;
        switch (scriptType) {
            case "groovy":
                script = repository.findById(1L);
                break;
            case "javaScript":
                script = repository.findById(2L);
                break;
            case "python":
                script = repository.findById(3L);
                break;
        }

        if (script != null) {
            script.setCode(code);
            repository.save(script);
        }
    }
}
