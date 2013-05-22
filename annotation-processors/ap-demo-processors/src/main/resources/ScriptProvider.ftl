<#--

    Copyright (c) 2013, Klaus Boeing & Michel Graciano.
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

    Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.

    Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

    Neither the name of the project's authors nor the names of its contributors
    may be used to endorse or promote products derived from this software without
    specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
    BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.

-->
package ${packageName};

import java.io.InputStreamReader;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.util.Scanner;
import tdc2013.script.ScriptProvider;

@javax.annotation.Generated(value = "tdc2013.repository.processors", date = "${date}")
public class ${interfaceName}ScriptsProvider {

    @Produces
    public ${interfacePath} getScript() throws ScriptException{
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("${engine}");
   
        engine.eval(getScript("${script}"));
        
        return Invocable.class.cast(engine).getInterface(${interfacePath}.class);
    }

    private String getScript(String script){
        String newValue = null;
        try {
            Class<ScriptProvider> clazz = (Class<ScriptProvider>) getClass().getClassLoader().loadClass(script);
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