package org.dcsa.tnt.itests.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@RequiredArgsConstructor
public class IntegrationTestsProperties {
  private static IntegrationTestsProperties instance;
  private final Properties properties;

  @Synchronized
  @SneakyThrows(IOException.class)
  public static IntegrationTestsProperties getInstance() {
    if (instance == null) {
      Properties properties = new Properties();
      try (InputStream is = ClassLoader.getSystemResourceAsStream("integration-tests.properties")) {
        properties.load(is);
        instance = new IntegrationTestsProperties(properties);
      }
    }
    return instance;
  }

  public String getBaseUri() {
    return properties.getProperty("base_uri");
  }

  public int getPort() {
    return Integer.parseInt(properties.getProperty("port"));
  }
}
