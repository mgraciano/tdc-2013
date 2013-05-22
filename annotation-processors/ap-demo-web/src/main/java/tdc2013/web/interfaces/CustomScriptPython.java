/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.web.interfaces;

import tdc2013.script.Script;

/**
 *
 * @author Klaus Boeing
 */
@Script(value = "tdc2013.web.ScriptProviderImpl$Python", engine = "python")
public interface CustomScriptPython {

    public int soma(int n1, int n2);

    public int diminui(int n1, int n2);

    public int multiplica(int n1, int n2);

    public Number divide(int n1, int n2);
}
