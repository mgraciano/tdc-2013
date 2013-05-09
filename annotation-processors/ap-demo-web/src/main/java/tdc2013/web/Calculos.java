/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.web;

import tdc2013.script.Script;

/**
 *
 * @author klaus.boeing
 */
@Script("/resources/js/Calculos.js")
public interface Calculos {
    public int soma(int n1, int n2);

    public int diminui(int n1, int n2);

    public int multiplica(int n1, int n2);

    public double divide(int n1, int n2);
}
