package org.dominokit.domino.apt.client.processors.test;

import com.google.auto.service.AutoService;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.test.api.client.annotations.TestConfig;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class TestConfigProcessor extends BaseProcessor {

  private final Set<String> supportedAnnotations = new HashSet<>();

  public TestConfigProcessor() {
    supportedAnnotations.add(TestConfig.class.getCanonicalName());
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return supportedAnnotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    new TestConfigProcessingStep.Builder()
            .setProcessingEnv(processingEnv)
            .build()
            .process(roundEnv.getElementsAnnotatedWith(TestConfig.class));
    return false;
  }

}
