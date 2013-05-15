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
package tdc2013.hibernate.processors;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.AbstractAnnotationValueVisitor7;

public abstract class AbstractAnnotationValueVisitor<R, P> extends AbstractAnnotationValueVisitor7<R, P> {

    @Override
    public R visitBoolean(boolean b, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitBoolean {0}...", b);
        return null;
    }

    @Override
    public R visitByte(byte b, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitByte {0}...", b);
        return null;
    }

    @Override
    public R visitChar(char c, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitChar {0}...", c);
        return null;
    }

    @Override
    public R visitDouble(double d, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitDouble {0}...", d);
        return null;
    }

    @Override
    public R visitFloat(float f, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitFloat {0}...", f);
        return null;
    }

    @Override
    public R visitInt(int i, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitInt {0}...", i);
        return null;
    }

    @Override
    public R visitLong(long i, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitLong {0}...", i);
        return null;
    }

    @Override
    public R visitShort(short s, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitShort {0}...", s);
        return null;
    }

    @Override
    public R visitString(String s, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitString {0}...", s);
        return null;
    }

    @Override
    public R visitType(TypeMirror t, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitType {0}...", t.toString());
        return null;
    }

    @Override
    public R visitEnumConstant(VariableElement c, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitEnumConstant {0}...", c.toString());
        return null;
    }

    @Override
    public R visitAnnotation(AnnotationMirror a, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitAnnotation {0}...", a.toString());
        return null;
    }

    @Override
    public R visitArray(List<? extends AnnotationValue> vals, P p) {
        Logger.getGlobal().log(Level.WARNING, "visitArray {0}...", vals.toString());
        return null;
    }
}
