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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class TypeValueProcessor extends AbstractProcessor {

    private static final String resourceName = "enum_value_names_apt_cache";
    //Need to be static because some APT limitations, mainly when using it inside tools like NetBeans
    private static final Set<String> registeredTypesName = new HashSet<>();
    //Using pretty printing for demonstration purpose
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        if (roundEnv.errorRaised()) {
            return false;
        }

        readCache();
        if (roundEnv.processingOver()) {
            writeCache();
        } else {
            handleProcess(annotations, roundEnv);
        }
        return false;
    }

    private void readCache() {
        final Filer filer = processingEnv.getFiler();
        try {
            final FileObject resource = filer.getResource(
                    StandardLocation.SOURCE_OUTPUT, "", resourceName);
            registeredTypesName.addAll(gson.<Set<String>>fromJson(resource.
                    openReader(true), new TypeToken<Set<String>>() {
                    }.getType()));
        } catch (FileNotFoundException ex) {
            /*
             * Suppressed exception because it gonna happen always the clean target is executed before the build
             */
        } catch (IOException ex) {
            final StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, sw.toString());
        }
    }

    private void writeCache() {
        final Filer filer = processingEnv.getFiler();

        try (Writer w = filer.createResource(
                StandardLocation.SOURCE_OUTPUT, "", resourceName).openWriter()) {
            w.append(gson.toJson(registeredTypesName));
            w.flush();
        } catch (IOException ex) {
            final StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Failed to write to " + resourceName + ": " + ex.toString()
                    + "\n" + sw.toString());
        }
    }

    private void handleProcess(final Set<? extends TypeElement> annotations,
            final RoundEnvironment env) {
        updateEnumValueCache(env);

        for (Element root : env.getRootElements()) {
            registeredTypesName.addAll(root.accept(new TypdeDefsVisitor(processingEnv), null));
        }

        final Elements elements = processingEnv.getElementUtils();
        for (Element element : env.getElementsAnnotatedWith(elements.getTypeElement("org.hibernate.annotations.Type"))) {
            for (AnnotationMirror am : element.getAnnotationMirrors()) {
                if (!am.getAnnotationType().asElement().getSimpleName().contentEquals("Type")) {
                    continue;
                }

                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().
                        entrySet()) {
                    final ExecutableElement ee = entry.getKey();
                    final AnnotationValue av = entry.getValue();
                    if (!ee.getSimpleName().contentEquals("type")) {
                        continue;
                    }

                    final String typeName = av.getValue().toString();
                    if (!registeredTypesName.contains(typeName)) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                                "O tipo " + typeName + " ainda não foi definido via TypeDefs", element, am, av);
                    }
                }
            }
        }
    }

    private void updateEnumValueCache(final RoundEnvironment env) {
    }

    @Override
    public Iterable<? extends Completion> getCompletions(final Element element,
            final AnnotationMirror annotation, final ExecutableElement member,
            final String userText) {
        final Elements elements = processingEnv.getElementUtils();
        final Types types = processingEnv.getTypeUtils();
        if (!types.isSameType(types.erasure(annotation.getAnnotationType()),
                types.erasure(elements.getTypeElement("org.hibernate.annotations.Type").asType()))) {
            return super.getCompletions(element, annotation, member, userText);
        }

        final Set<Completion> c = new LinkedHashSet<>(registeredTypesName.size());
        for (final String namedQuery : registeredTypesName) {
            c.add(new Completion() {
                @Override
                public String getValue() {
                    return '\"' + namedQuery + '\"';
                }

                @Override
                public String getMessage() {
                    return null;
                }
            });
        }
        return c;
    }
}
