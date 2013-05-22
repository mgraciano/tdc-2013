/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.web;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import javax.faces.context.FacesContext;
import tdc2013.script.ScriptProvider;

/**
 *
 * @author Klaus Boeing
 */
public class Teste {
    public static void main(String[] args) {
        new InputStreamReader(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("${script}"));
        
    }
    
    private String getScript(String value){
        String newValue = value;
        try {
            Class<ScriptProvider> clazz = (Class<ScriptProvider>) Thread.currentThread().getContextClassLoader().loadClass(value);
            ScriptProvider provider = clazz.newInstance();
            newValue = provider.getScript();
        } catch (Exception ex) {
            InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("${script}");
            if(is == null){
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("${script}");
            }
            
            newValue = new Scanner(is).useDelimiter("\\Z").next();
        }
        return newValue;
    }
}
