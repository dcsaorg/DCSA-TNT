package org.dcsa.tnt.itests.config;

import io.restassured.RestAssured;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestAssuredConfigurator {

  public void initialize() {
    var properties = IntegrationTestsProperties.getInstance();
    RestAssured.baseURI = properties.getBaseUri();
    RestAssured.port = properties.getPort();
  }
}
