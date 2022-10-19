package org.dcsa.tnt.itests.v3;

import io.restassured.http.ContentType;
import org.dcsa.tnt.itests.config.RestAssuredConfigurator;
import org.dcsa.tnt.transferobjects.*;
import org.dcsa.tnt.transferobjects.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.ReferenceType;
import org.dcsa.tnt.transferobjects.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.TransportEventTypeCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class GetEventsIT {
  @BeforeAll
  public static void initializeRestAssured() {
    RestAssuredConfigurator.initialize();
  }

  @Test
  public void getAllEVents() {
    given()
      .contentType("application/json")
      .get("/v3/events")
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
        .get("/v3/events?eventType=SHIPMENT")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(13))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertInstanceOf(ShipmentEventPayloadTO.class, e.payload()));
  }

  @Test
  public void getEquipmentEvents() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?eventType=EQUIPMENT")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(2))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertInstanceOf(EquipmentEventPayloadTO.class, e.payload()));
  }

  @Test
  public void getTransportEvents() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?eventType=TRANSPORT")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(8))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertInstanceOf(TransportEventPayloadTO.class, e.payload()));
  }

  @Test
  public void getByShipmentEventTypeCode() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?shipmentEventTypeCode=RECE")
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
      ShipmentEventPayloadTO payload = assertInstanceOf(ShipmentEventPayloadTO.class, e.payload());
      assertEquals(ShipmentEventTypeCode.RECE, payload.getShipmentEventTypeCode());
    });
  }

  @Test
  public void getByDocumentReferenceCBR() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?documentReference=cbr-b83765166707812c8ff4")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(1))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertTrue(e.payload().getRelatedDocumentReferences().stream()
      .anyMatch(docRef -> "cbr-b83765166707812c8ff4".equals(docRef.value()))
    ));
  }

  @Test
  public void getByDocumentReferenceTRD() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?documentReference=2b02401c-b2fb-5009")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(1))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertTrue(e.payload().getRelatedDocumentReferences().stream()
      .anyMatch(docRef -> "2b02401c-b2fb-5009".equals(docRef.value()))
    ));
  }

  @Test
  public void getByTransportEventTypeCode() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?transportEventTypeCode=DEPA")
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
      TransportEventPayloadTO transportEventPayload = assertInstanceOf(TransportEventPayloadTO.class, e.payload());
      assertEquals(TransportEventTypeCode.DEPA, transportEventPayload.getTransportEventTypeCode());
    });
  }

  @Test
  public void getByTransportCallReference() {
    final String transportCallReference = "TC-REF-08_02-A";
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?transportCallReference=" + transportCallReference)
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
      EventPayloadTO.EventPayloadTOWithTransportCall payload = assertInstanceOf(
        EventPayloadTO.EventPayloadTOWithTransportCall.class,
        e.payload()
      );
      assertEquals(transportCallReference, payload.getTransportCall().transportCallReference());
    });
  }

  @Test
  public void getByVesselIMONumber() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?vesselIMONumber=1234567")
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
      EventPayloadTO.EventPayloadTOWithTransportCall payload = assertInstanceOf(
        EventPayloadTO.EventPayloadTOWithTransportCall.class,
        e.payload()
      );
      assertEquals("1234567", payload.getTransportCall().vessel().vesselIMONumber());
    });
  }

  @Test
  public void getByCarrierVoyageNumber1() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?carrierExportVoyageNumber=TNT1E")
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
      EventPayloadTO.EventPayloadTOWithTransportCall payload = assertInstanceOf(
        EventPayloadTO.EventPayloadTOWithTransportCall.class,
        e.payload()
      );

      String exportCarrierVoyageNumber = payload.getTransportCall().carrierExportVoyageNumber();
      assertEquals("TNT1E", exportCarrierVoyageNumber);
    });
  }

  @Test
  public void getByCarrierVoyageNumber2() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?carrierExportVoyageNumber=3419E")
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
      EventPayloadTO.EventPayloadTOWithTransportCall payload = assertInstanceOf(
        EventPayloadTO.EventPayloadTOWithTransportCall.class,
        e.payload()
      );

      String exportCarrierVoyageNumber = payload.getTransportCall().carrierExportVoyageNumber();
      assertEquals("3419E", exportCarrierVoyageNumber);
    });
  }

  @Test
  public void getByCarrierServiceCode() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?carrierServiceCode=TNT1")
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
      EventPayloadTO.EventPayloadTOWithTransportCall payload = assertInstanceOf(
        EventPayloadTO.EventPayloadTOWithTransportCall.class,
        e.payload()
      );
      String carrierServiceCode = payload.getTransportCall().carrierServiceCode();
      assertEquals("TNT1", carrierServiceCode);
    });
  }

  @Test
  public void getByUniversalServiceReference() {
    final String usr = "SR00033F";
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?universalServiceReference=" + usr)
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
      EventPayloadTO.EventPayloadTOWithTransportCall payload = assertInstanceOf(
        EventPayloadTO.EventPayloadTOWithTransportCall.class,
        e.payload()
      );
      String universalServiceReference = payload.getTransportCall().universalServiceReference();
      assertEquals(usr, universalServiceReference);
    });
  }

  @Test
  public void getByEquipmentEventTypeCode() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?equipmentEventTypeCode=LOAD")
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
      EquipmentEventPayloadTO equipmentEventPayload = assertInstanceOf(EquipmentEventPayloadTO.class, e.payload());
      assertEquals(EquipmentEventTypeCode.LOAD, equipmentEventPayload.getEquipmentEventTypeCode());
    });
  }

  @Test
  public void getByEquipmentReference() {
    String equipmentReference = "APZU4812090";

    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?equipmentReference=" + equipmentReference)
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
      EventPayloadTO payload = e.payload();
      boolean presentInReferences = payload.getReferences() != null && payload.getReferences().stream()
        .anyMatch(ref -> ref.type() == ReferenceType.EQ && equipmentReference.equals(ref.value()));
      boolean equipmentEventReference = payload instanceof EquipmentEventPayloadTO equipmentPayload
        && equipmentReference.equals(equipmentPayload.getEquipmentReference());
      assertTrue(presentInReferences ||equipmentEventReference);
    });
  }


  @Test
  public void getTransportEventCombo() {
    List<EventTO> events =
      given()
        .contentType("application/json")
        .get("/v3/events?eventType=TRANSPORT&transportCallID=123e4567-e89b-12d3-a456-426614174000&vesselIMONumber=9321483&carrierVoyageNumber=TNT1E")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(1))
        .extract()
        .body()
        .jsonPath()
        .getList(".", EventTO.class);

    events.forEach(e -> assertInstanceOf(TransportEventPayloadTO.class, e.payload()));
  }

  @Test
  public void getImpossibleCombo() {
    given()
      .contentType("application/json")
      .get("/v3/events?shipmentEventTypeCode=RECE&equipmentReference=APZU4812090")
      .then()
      .assertThat()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("size()", equalTo(0));
  }
}
