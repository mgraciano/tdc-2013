/*
 * Copyright (c) 2013, Klaus López Boeing & Michel Graciano.
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
package tdc2013.repository.processors;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

/**
 * @author Klaus Boeing
 */
@SupportedAnnotationTypes("tdc2013.repository.Repository")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RepositoryProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || annotations.isEmpty()) {
            return true;
        }

        TypeElement typeElement = annotations.iterator().next();

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(typeElement);

        for (Element _e : elements) {
            TypeElement classElement = (TypeElement) _e;
            PackageElement packageElement =
                    (PackageElement) classElement.getEnclosingElement();

            try {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        classElement.getQualifiedName() + "Impl");

                BufferedWriter bw = new BufferedWriter(jfo.openWriter());
                bw.append("package ");
                bw.append(packageElement.getQualifiedName());
                bw.append(";");
                bw.newLine();
                bw.newLine();

                bw.append("public class " + classElement.getSimpleName() + "Impl implements " + classElement.getQualifiedName() + "{");

                bw.newLine();
                bw.newLine();
                for (Element _element : classElement.getEnclosedElements()) {
                    if (_element.getKind() == ElementKind.METHOD) {
                        ExecutableElement method = ExecutableElement.class.cast(_element);
                        bw.append("public " + method.getReturnType());
                        bw.append(" " + method.getSimpleName());
                        bw.append("(");

                        for (int i = 0; i < method.getParameters().size(); i++) {
                            VariableElement typeParameterElement = method.getParameters().get(i);

                            bw.append(" " + typeParameterElement.asType());
                            bw.append(" " + typeParameterElement.getSimpleName());

                            if (i + 1 < method.getParameters().size()) {
                                bw.append(",");
                            }
                        }


                        bw.append("){");
                        bw.append("System.out.println(\"" + method.getSimpleName() + "\");");
                        bw.newLine();
                        bw.append("return null;");
                        bw.newLine();
                        bw.append("}");
                    }
                    bw.newLine();
                }

                bw.newLine();
                bw.append("}");

                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(RepositoryProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
}
