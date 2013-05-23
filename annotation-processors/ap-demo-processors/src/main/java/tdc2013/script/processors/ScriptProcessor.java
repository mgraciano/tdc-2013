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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.script.processors;

import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import tdc2013.script.Script;
import tdc2013.util.FreemarkerUtils;

@SupportedAnnotationTypes("tdc2013.script.Script")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ScriptProcessor extends AbstractProcessor {

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
                        classElement.getQualifiedName() + "ScriptsProvider");

                String script = classElement.getAnnotation(Script.class).value();
                String engine = classElement.getAnnotation(Script.class).engine();
                String url = classElement.getAnnotation(Script.class).url();

                ScriptInfo info = new ScriptInfo(packageElement.getQualifiedName().toString(), classElement.getSimpleName().toString(), classElement.getQualifiedName().toString(), script, engine, url);

                try (BufferedWriter bw = new BufferedWriter(jfo.openWriter())) {
                    FreemarkerUtils.parseTemplate(bw, info, "ScriptProvider.ftl");
                } catch (TemplateException ex) {
                    Logger.getLogger(tdc2013.repository.processors.RepositoryProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (!url.isEmpty()) {
                    JavaFileObject jfow = processingEnv.getFiler().createSourceFile(
                        info.getPackageName().concat(".").concat(info.getServletName()).concat("Servlet"));

                    try (BufferedWriter bw = new BufferedWriter(jfow.openWriter())) {
                        FreemarkerUtils.parseTemplate(bw, info, "ScriptServlet.ftl");
                    } catch (TemplateException ex) {
                        Logger.getLogger(tdc2013.repository.processors.RepositoryProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(tdc2013.repository.processors.RepositoryProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation,
            ExecutableElement member, String userText) {
        if (!member.getSimpleName().contentEquals("engine")) {
            return super.getCompletions(element, annotation, member, userText);
        }

        return Arrays.asList(new Completion() {
            @Override
            public String getValue() {
                return "\"js\"";
            }

            @Override
            public String getMessage() {
                return "Motor de script JavaScript padrão da plataforma.";
            }
        }, new Completion() {
            @Override
            public String getValue() {
                return "\"javascript\"";
            }

            @Override
            public String getMessage() {
                return "Motor de script JavaScript padrão da plataforma.";
            }
        }, new Completion() {
            @Override
            public String getValue() {
                return "\"nashorn\"";
            }

            @Override
            public String getMessage() {
                return "Motor de script JavaScript Nashorn, desenvolvido pela Oracle.";
            }
        });
    }
}
