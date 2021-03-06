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

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class TypdeDefsVisitor extends AbstractElementVisitor<Set<String>, Void> {

    public TypdeDefsVisitor(ProcessingEnvironment processingEnv) {
        super(processingEnv, Collections.<String>emptySet());
    }

    @Override
    public Set<String> visitPackage(PackageElement e, Void p) {
        final TypdeDefsInfo info = new TypdeDefsInfo(e);

        Logger.getGlobal().log(Level.WARNING, "PackageElement {0}...", e.toString());
        for (AnnotationMirror am : e.getAnnotationMirrors()) {
            if (!am.getAnnotationType().asElement().getSimpleName().contentEquals("TypeDefs")) {
                continue;
            }

            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().
                    entrySet()) {
                final AnnotationValue typeDef = entry.getValue();
                typeDef.accept(new TypeDefNameVisitor(), info);
                typeDef.accept(new TypeDefEnumClassVisitor(processingEnv), info);
            }
        }

        Logger.getGlobal().log(Level.WARNING, "Names {0}...", info.toString());
        return info.getTypesNames();
    }
}

class TypeDefNameVisitor extends AbstractTypeDefVisitor<Void, TypdeDefsInfo> {

    @Override
    public Void visitAnnotation(AnnotationMirror a, TypdeDefsInfo info) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : a.getElementValues().entrySet()) {
            final ExecutableElement ee = entry.getKey();
            if (ee.getSimpleName().contentEquals("name")) {
                info.getTypesNames().add(entry.getValue().getValue().toString());
            }
        }
        return null;
    }

}

class TypeDefEnumClassVisitor extends AbstractTypeDefVisitor<Void, TypdeDefsInfo> {

    private final ProcessingEnvironment processingEnv;

    TypeDefEnumClassVisitor(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    @Override
    public Void visitAnnotation(final AnnotationMirror a, final TypdeDefsInfo info) {
        visitTypeDef(a, info);
        visitTypeDefParameterAnn(a, info);
        return null;
    }

    private void visitTypeDef(final AnnotationMirror a, final TypdeDefsInfo info) {
        if (!isSameType(a.getAnnotationType(), "org.hibernate.annotations.TypeDef")) {
            return;
        }

        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : a.getElementValues().entrySet()) {
            final ExecutableElement ee = entry.getKey();
            if (ee.getSimpleName().contentEquals("typeClass") && !entry.getValue().getValue().toString().equals(
                    "tdc2013.hibernate.EnumValueUserType")) {
                return;
            }
        }

        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : a.getElementValues().entrySet()) {
            final ExecutableElement ee = entry.getKey();
            if (ee.getSimpleName().contentEquals("parameters")) {
                entry.getValue().accept(this, info);
            }
        }
    }

    private void visitTypeDefParameterAnn(final AnnotationMirror a, final TypdeDefsInfo info) {
        if (!isSameType(a.getAnnotationType(), "org.hibernate.annotations.Parameter")) {
            return;
        }

        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : a.getElementValues().entrySet()) {
            final ExecutableElement ee = entry.getKey();
            if (ee.getSimpleName().contentEquals("value")) {
                final String enumClassName = entry.getValue().getValue().toString();
                if (!isSubtype(enumClassName, "tdc2013.hibernate.EnumValue")) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            enumClassName + " deve implementar tdc2013.hibernate.EnumValue.", info.getRootElement(), a,
                            entry.getValue());
                }
            }
        }
    }

    private boolean isSubtype(final String t1, final String t2) {
        final Elements elements = processingEnv.getElementUtils();
        final Types types = processingEnv.getTypeUtils();
        return types.isSubtype(types.erasure(elements.getTypeElement(t1).asType()), types.erasure(elements.
                getTypeElement(t2).asType()));
    }

    private boolean isSameType(final TypeMirror at, final String type) {
        final Elements elements = processingEnv.getElementUtils();
        final Types types = processingEnv.getTypeUtils();
        return types.isSameType(types.erasure(at), types.erasure(elements.getTypeElement(type).asType()));
    }
}
