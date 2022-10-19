package org.dcsa.tnt.itests.v3;

import io.restassured.http.ContentType;
import org.dcsa.tnt.itests.config.RestAssuredConfigurator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HealthCheckIT {
  @BeforeAll
  public static void initializeRestAssured() {
    RestAssuredConfigurator.initialize();
  }

  @Test
  public void testHealth() {
    given()
      .contentType("application/json")
      .get("/v3/actuator/health")
      .then()
      .assertThat()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("status", equalTo("UP"))
    ;
  }
}
