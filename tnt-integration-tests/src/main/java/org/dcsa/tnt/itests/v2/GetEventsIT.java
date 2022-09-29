package org.dcsa.tnt.itests.v2;

import io.restassured.http.ContentType;
import org.dcsa.tnt.itests.config.RestAssuredConfigurator;
import org.dcsa.tnt.transferobjects.EquipmentEventTO;
import org.dcsa.tnt.transferobjects.EventTO;
import org.dcsa.tnt.transferobjects.EventTOWithTransportCallTO;
import org.dcsa.tnt.transferobjects.ShipmentEventTO;
import org.dcsa.tnt.transferobjects.TransportEventTO;
import org.dcsa.tnt.transferobjects.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.TransportEventTypeCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetEventsIT {
  @BeforeAll
  public static void initializeRestAssured() {
    RestAssuredConfigurator.initialize();
  }

  @Test
  public void getAllEVents() {
    given()
      .contentType("application/json")
      .get("/v2/events")
      .then()
      .assertThat()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("size()", greaterThanOrEqualTo(23))
    ;
  }

  @Test
  public void getShipmentEvents() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?eventType=SHIPMENT")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(13))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertInstanceOf(ShipmentEventTO.class, e));
  }

  @Test
  public void getEquipmentEvents() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?eventType=EQUIPMENT")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(2))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertInstanceOf(EquipmentEventTO.class, e));
  }

  @Test
  public void getTransportEvents() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?eventType=TRANSPORT")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(8))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertInstanceOf(TransportEventTO.class, e));
  }

  @Test
  public void getByShipmentEventTypeCode() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?shipmentEventTypeCode=RECE")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(5))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> {
      assertInstanceOf(ShipmentEventTO.class, e);
      ShipmentEventTO shipmentEvent = (ShipmentEventTO) e;
      assertEquals(ShipmentEventTypeCode.RECE, shipmentEvent.getShipmentEventTypeCode());
    });
  }

  @Test
  public void getByCarrierBookingReference() {
    // TODO https://dcsa.atlassian.net/browse/DDT-1253
  }

  @Test
  public void getByTransportDocumentReference() {
    // TODO https://dcsa.atlassian.net/browse/DDT-1253
  }

  @Test
  public void getByTransportEventTypeCode() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?transportEventTypeCode=DEPA")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(5))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> {
      assertInstanceOf(TransportEventTO.class, e);
      TransportEventTO transportEvent = (TransportEventTO) e;
      assertEquals(TransportEventTypeCode.DEPA, transportEvent.getTransportEventTypeCode());
    });
  }

  @Test
  public void getByTransportCallId() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?transportCallID=123e4567-e89b-12d3-a456-426614174000")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(2))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> {
      assertInstanceOf(EventTOWithTransportCallTO.class, e);
      EventTOWithTransportCallTO event = (EventTOWithTransportCallTO) e;
      assertEquals("123e4567-e89b-12d3-a456-426614174000", event.getTransportCall().id().toString());
    });
  }

  @Test
  public void getByVesselIMONumber() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?vesselIMONumber=1234567")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(2))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> {
      assertInstanceOf(EventTOWithTransportCallTO.class, e);
      EventTOWithTransportCallTO event = (EventTOWithTransportCallTO) e;
      assertEquals("1234567", event.getTransportCall().vessel().vesselIMONumber());
    });
  }

  @Test
  public void getByCarrierVoyageNumber1() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?carrierVoyageNumber=2418W")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(1))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> {
      assertInstanceOf(EventTOWithTransportCallTO.class, e);
      EventTOWithTransportCallTO event = (EventTOWithTransportCallTO) e;

      String importCarrierVoyageNumber = event.getTransportCall().importVoyage().carrierVoyageNumber();
      String exportCarrierVoyageNumber = event.getTransportCall().exportVoyage().carrierVoyageNumber();
      assertTrue("2418W".equals(importCarrierVoyageNumber) || "2418W".equals(exportCarrierVoyageNumber));
    });
  }

  @Test
  public void getByCarrierVoyageNumber2() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?carrierVoyageNumber=2419E")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(1))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> {
      assertInstanceOf(EventTOWithTransportCallTO.class, e);
      EventTOWithTransportCallTO event = (EventTOWithTransportCallTO) e;

      String importCarrierVoyageNumber = event.getTransportCall().importVoyage().carrierVoyageNumber();
      String exportCarrierVoyageNumber = event.getTransportCall().exportVoyage().carrierVoyageNumber();
      assertTrue("2419E".equals(importCarrierVoyageNumber) || "2419E".equals(exportCarrierVoyageNumber));
    });
  }

  @Test
  public void getByCarrierServiceCode() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?carrierServiceCode=TNT1")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(2))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> {
      assertInstanceOf(EventTOWithTransportCallTO.class, e);
      EventTOWithTransportCallTO event = (EventTOWithTransportCallTO) e;

      String importCarrierServiceCode = event.getTransportCall().importVoyage().service().carrierServiceCode();
      String exportCarrierServiceCode = event.getTransportCall().exportVoyage().service().carrierServiceCode();
      assertTrue("TNT1".equals(importCarrierServiceCode) || "TNT1".equals(exportCarrierServiceCode));
    });
  }

  @Test
  public void getByEquipmentEventTypeCode() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?equipmentEventTypeCode=LOAD")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(2))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> {
      assertInstanceOf(EquipmentEventTO.class, e);
      EquipmentEventTO equipmentEvent = (EquipmentEventTO) e;
      assertEquals(EquipmentEventTypeCode.LOAD, equipmentEvent.getEquipmentEventTypeCode());
    });
  }

  @Test
  public void getByEquipmentReference() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?equipmentReference=APZU4812090")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(1))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> {
      assertInstanceOf(EquipmentEventTO.class, e);
      EquipmentEventTO equipmentEvent = (EquipmentEventTO) e;
      assertEquals("APZU4812090", equipmentEvent.getEquipmentReference());
    });
  }

  @Test
  public void getTransportEventCombo() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v2/events?eventType=TRANSPORT&transportCallID=123e4567-e89b-12d3-a456-426614174000&vesselIMONumber=9321483&carrierVoyageNumber=TNT1E")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(1))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertInstanceOf(TransportEventTO.class, e));
  }

  @Test
  public void getImpossibleCombo() {
    given()
      .contentType("application/json")
      .get("/v2/events?shipmentEventTypeCode=RECE&equipmentReference=APZU4812090")
      .then()
      .assertThat()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("size()", equalTo(0));
  }
}
