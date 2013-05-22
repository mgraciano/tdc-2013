/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.web.interfaces;

import java.math.BigDecimal;
import tdc2013.script.Script;

/**
 *
 * @author Klaus Boeing
 */
@Script(value = "tdc2013.web.ScriptProviderImpl$Groovy", engine = "groovy")
public interface CustomScriptGroovy  {

    public int soma(int n1, int n2);

    public int diminui(int n1, int n2);

    public int multiplica(int n1, int n2);

    public BigDecimal divide(int n1, int n2);
}
