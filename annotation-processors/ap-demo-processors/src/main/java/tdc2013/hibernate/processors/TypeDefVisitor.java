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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.AbstractAnnotationValueVisitor7;

public class TypeDefVisitor extends AbstractAnnotationValueVisitor7<Void, Void> {

    @Override
    public Void visitBoolean(boolean b, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitBoolean {0}...", b);
        return null;
    }

    @Override
    public Void visitByte(byte b, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitByte {0}...", b);
        return null;
    }

    @Override
    public Void visitChar(char c, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitChar {0}...", c);
        return null;
    }

    @Override
    public Void visitDouble(double d, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitDouble {0}...", d);
        return null;
    }

    @Override
    public Void visitFloat(float f, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitFloat {0}...", f);
        return null;
    }

    @Override
    public Void visitInt(int i, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitInt {0}...", i);
        return null;
    }

    @Override
    public Void visitLong(long i, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitLong {0}...", i);
        return null;
    }

    @Override
    public Void visitShort(short s, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitShort {0}...", s);
        return null;
    }

    @Override
    public Void visitString(String s, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitString {0}...", s);
        return null;
    }

    @Override
    public Void visitType(TypeMirror t, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitType {0}...", t.toString());
        return null;
    }

    @Override
    public Void visitEnumConstant(VariableElement c, Void p) {
        Logger.getGlobal().log(Level.WARNING, "visitEnumConstant {0}...", c.toString());
        return null;
    }

    @Override
    public Void visitAnnotation(AnnotationMirror a, Void p) {
//        Logger.getGlobal().log(Level.WARNING, "visitAnnotation {0}...", a.toString());
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : a.getElementValues().entrySet()) {
//            entry.getValue().accept(this, null);
            Logger.getGlobal().log(Level.WARNING, "AV {0}...", entry.toString());
        }
        return null;
    }

    @Override
    public Void visitArray(
            List<? extends AnnotationValue> vals, Void p) {
//        Logger.getGlobal().log(Level.WARNING, "visitArray {0}...", vals.toString());
        for (AnnotationValue av : vals) {
            av.accept(this, null);
        }
        return null;
    }
}
