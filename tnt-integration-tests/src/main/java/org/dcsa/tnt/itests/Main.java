package org.dcsa.tnt.itests;

import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.discovery.LauncherDiscoveryListeners;

import java.io.PrintWriter;

public class Main {

  public static void main(String[] args) {
    System.out.println("Running integration tests..");

    var summary = runTests();
    if (summary.getContainersFailedCount() > 0 || summary.getTestsFailedCount() > 0) {
      summary.printFailuresTo(new PrintWriter(System.out));
      System.exit(1);
    }
    System.exit(0);
  }

  private static TestExecutionSummary runTests() {
    var listener = new SummaryGeneratingListener();

    var launcher = LauncherFactory.create();
    launcher.registerLauncherDiscoveryListeners(
      LauncherDiscoveryListeners.logging(),
      LauncherDiscoveryListeners.abortOnFailure()
    );

    var testplan = launcher.discover(
      LauncherDiscoveryRequestBuilder.request()
        .selectors(DiscoverySelectors.selectPackage(Main.class.getPackageName()))
        .filters(ClassNameFilter.includeClassNamePatterns(".*(Test|IT)"))
        .build()
    );
    launcher.registerTestExecutionListeners(listener);
    launcher.execute(testplan);

    listener.getSummary().printTo(new PrintWriter(System.out));
    return listener.getSummary();
  }
}
