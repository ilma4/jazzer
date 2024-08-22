/*
 * Copyright 2024 Code Intelligence GmbH
 *
 * By downloading, you agree to the Code Intelligence Jazzer Terms and Conditions.
 *
 * The Code Intelligence Jazzer Terms and Conditions are provided in LICENSE-JAZZER.txt
 * located in the root directory of the project.
 */

package com.code_intelligence.jazzer.junit;

import static com.code_intelligence.jazzer.junit.Utils.getClassPathBasedInstrumentationFilter;
import static com.code_intelligence.jazzer.junit.Utils.getLegacyInstrumentationFilter;
import static com.code_intelligence.jazzer.junit.Utils.isGatheringCoverage;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import com.code_intelligence.jazzer.driver.Opt;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.extension.ExtensionContext;

class AgentConfigurator {
  private static final AtomicBoolean hasBeenConfigured = new AtomicBoolean();

  static void forRegressionTest(ExtensionContext extensionContext) {
    if (!hasBeenConfigured.compareAndSet(false, true)) {
      return;
    }

    applyCommonConfiguration(extensionContext);

    // Add logic to the hook instrumentation that allows us to enable and disable hooks at runtime.
    Opt.conditionalHooks.setIfDefault(true);
    Opt.instrument.setIfDefault(determineInstrumentationFilters(extensionContext));
    // Apply all hooks, but no coverage or compare instrumentation.
    Opt.instrumentationExcludes.setIfDefault(singletonList("**"));
    Opt.customHookIncludes.setIfDefault(Opt.instrument.get());
  }

  static void forFuzzing(ExtensionContext extensionContext) {
    if (!hasBeenConfigured.compareAndSet(false, true)) {
      throw new IllegalStateException("Only a single fuzz test should be executed per fuzzing run");
    }

    applyCommonConfiguration(extensionContext);

    Opt.instrument.setIfDefault(determineInstrumentationFilters(extensionContext));
    Opt.customHookIncludes.setIfDefault(Opt.instrument.get());
    Opt.instrumentationIncludes.setIfDefault(Opt.instrument.get());

    if (Opt.junitInstrumentationExcludeTargetPackage.get()
        && !Opt.instrumentationExcludes.isSet()) {
      String pkg = extensionContext.getRequiredTestClass().getPackage().getName();
      Opt.instrumentationExcludes.setIfDefault(Collections.singletonList(pkg + ".**"));
    }
  }

  private static List<String> determineInstrumentationFilters(ExtensionContext extensionContext) {
    return getClassPathBasedInstrumentationFilter(System.getProperty("java.class.path"))
        .orElseGet(() -> getLegacyInstrumentationFilter(extensionContext.getRequiredTestClass()));
  }

  private static void applyCommonConfiguration(ExtensionContext extensionContext) {
    Opt.registerConfigurationParameters(extensionContext::getConfigurationParameter);
    // Do not hook common IDE and JUnit classes and their dependencies.
    List<String> hookExcludes =
        asList(
            "com.google.testing.junit.**",
            "com.intellij.**",
            "org.jetbrains.**",
            "io.github.classgraph.**",
            "junit.framework.**",
            "net.bytebuddy.**",
            "org.apiguardian.**",
            "org.assertj.core.**",
            "org.hamcrest.**",
            "org.junit.**",
            "org.opentest4j.**",
            "org.mockito.**",
            "org.apache.maven.**",
            "org.gradle.**");
    if (Opt.junitCustomHooksExcludeTargetPackage.get()) {
      String pkg = extensionContext.getRequiredTestClass().getPackage().getName();
      hookExcludes.add(pkg + ".**");
    }
    Opt.customHookExcludes.setIfDefault(hookExcludes);
    if (isGatheringCoverage()) {
      // The IntelliJ coverage agent uses regular expressions in its instrumentor to determine which
      // classes to instrument and thus triggers our regex hook when it is asked to instrument
      // JazzerInternal, which in turn loads JazzerInternal and thus results in a
      // ClassCircularityError.
      // Instead of disabling the hook and just possibly altering how fuzz tests behave between test
      // and coverage runs, we could instead move all runtime hooks into one of the packages on
      // this hard-coded list:
      // https://github.com/JetBrains/intellij-coverage/blob/49d19a968f23a9ad929131655a97790cf18fdf44/util/src/com/intellij/rt/coverage/instrumentation/AbstractIntellijClassfileTransformer.java#L79-L85
      Opt.additionalClassesExcludes.setIfDefault(asList("java.util.regex.**"));
    }
  }
}
