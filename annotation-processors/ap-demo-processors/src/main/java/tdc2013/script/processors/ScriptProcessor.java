/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdc2013.script.processors;

import freemarker.template.TemplateException;
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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import tdc2013.repository.processors.FreemarkerUtils;
import tdc2013.script.Script;

/**
 *
 * @author klaus.boeing
 */
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
                
                ScriptInfo info = new ScriptInfo(packageElement.getQualifiedName().toString(), classElement.getSimpleName().toString(), classElement.getQualifiedName().toString(), script, engine);

                try (BufferedWriter bw = new BufferedWriter(jfo.openWriter())) {
                    FreemarkerUtils.parseTemplate(bw, info, "ScriptProvider.ftl");
                } catch (TemplateException ex) {
                    Logger.getLogger(tdc2013.repository.processors.RepositoryProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(tdc2013.repository.processors.RepositoryProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
}
