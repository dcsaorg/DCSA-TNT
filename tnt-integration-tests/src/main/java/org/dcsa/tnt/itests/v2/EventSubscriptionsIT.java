package org.dcsa.tnt.itests.v2;

import io.restassured.http.ContentType;
import org.dcsa.tnt.itests.config.RestAssuredConfigurator;
import org.dcsa.tnt.transferobjects.EventSubscriptionSecretTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithIdTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithSecretTO;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import org.dcsa.tnt.transferobjects.enums.DocumentTypeCode;
import org.dcsa.tnt.transferobjects.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.EventType;
import org.dcsa.tnt.transferobjects.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.TransportEventTypeCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class EventSubscriptionsIT {
  @BeforeAll
  public static void initializeRestAssured() {
    RestAssuredConfigurator.initialize();
  }

  @Test
  public void testCreateAndRetrieveSingle() {
    EventSubscriptionWithSecretTO eventSubscription = eventSubscriptionWithSecretTO();

    EventSubscriptionWithIdTO created = create(eventSubscription);
    EventSubscriptionWithIdTO retrieved = retrieve(created.getSubscriptionID());

    assertEventSubscriptionTOEquals(eventSubscription, created);
    assertEquals(created, retrieved);
  }

  @Test
  public void testCreateAndRetrieveList() {
    EventSubscriptionWithSecretTO eventSubscription = eventSubscriptionWithSecretTO();

    EventSubscriptionWithIdTO created = create(eventSubscription);

    List<EventSubscriptionWithIdTO> list = given()
      .contentType("application/json")
      .get("/v2/event-subscriptions")
      .then()
      .assertThat()
      .statusCode(HttpStatus.OK.value())
      .contentType(ContentType.JSON)
      .body("size()", greaterThanOrEqualTo(1))
      .extract()
      .body()
      .jsonPath()
      .getList(".", EventSubscriptionWithIdTO.class)
      ;

    EventSubscriptionWithIdTO retrieved = list.stream()
      .filter(event -> created.getSubscriptionID().equals(event.getSubscriptionID()))
      .findAny().get();
    assertEventSubscriptionTOEquals(eventSubscription, created);
    assertEquals(created, retrieved);
  }

  @Test
  public void testUpdate() {
    EventSubscriptionWithSecretTO eventSubscription = eventSubscriptionWithSecretTO();
    EventSubscriptionTO other = EventSubscriptionTO.builder()
      .callbackUrl("http://other.url")
      .documentReference("otherDRef")
      .equipmentReference("otherEQRef")
      .transportCallReference("otherTCRef")
      .carrierExportVoyageNumber("other-car")
      .universalExportVoyageReference("35BQE")
      .carrierServiceCode("csc78")
      .universalServiceReference("SR56789F")
      .UNLocationCode("locUN")
      .eventTypes(inverseSet(eventTypes, EventType.class))
      .shipmentEventTypeCodes(inverseSet(shipmentEventTypeCodes, ShipmentEventTypeCode.class))
      .documentTypeCodes(inverseSet(documentTypeCodes, DocumentTypeCode.class))
      .transportEventTypeCodes(inverseSet(transportEventTypeCodes, TransportEventTypeCode.class))
      .equipmentEventTypeCodes(inverseSet(equipmentEventTypeCodes, EquipmentEventTypeCode.class))
      .build();

    EventSubscriptionWithIdTO created = create(eventSubscription);
    update(created.getSubscriptionID(), other);

    EventSubscriptionWithIdTO retrieved = retrieve(created.getSubscriptionID());
    assertEventSubscriptionTOEquals(other, retrieved);
  }

  @Test
  public void testUpdateWithNulls() {
    EventSubscriptionWithSecretTO eventSubscription = eventSubscriptionWithSecretTO();
    EventSubscriptionTO withNulls = EventSubscriptionTO.builder()
      .callbackUrl(callbackUrl) // mandatory
      .build();

    EventSubscriptionWithIdTO created = create(eventSubscription);
    update(created.getSubscriptionID(), withNulls);

    EventSubscriptionWithIdTO retrieved = retrieve(created.getSubscriptionID());
    assertEventSubscriptionTOEquals(withNulls, retrieved);
  }

  @Test
  public void testUpdateSecret() {
    EventSubscriptionWithSecretTO eventSubscription = eventSubscriptionWithSecretTO();
    EventSubscriptionWithIdTO created = create(eventSubscription);
    String newSecret = "another secret - not the same as before";

    given()
      .contentType("application/json")
      .body(new EventSubscriptionSecretTO(newSecret.getBytes(StandardCharsets.UTF_8)))
      .put("/v2/event-subscriptions/" + created.getSubscriptionID() + "/secret")
      .then()
      .assertThat()
      .statusCode(HttpStatus.OK.value())
    ;

    given()
      .contentType("application/json")
      .get("/v2/unofficial/event-subscriptions-dao/" + created.getSubscriptionID())
      .then()
      .assertThat()
      .statusCode(HttpStatus.OK.value())
      .contentType(ContentType.JSON)
      .body("secret", equalTo(Base64.getEncoder().encodeToString(newSecret.getBytes(StandardCharsets.UTF_8))))
    ;
  }

  @Test
  public void testDelete() {
    EventSubscriptionWithSecretTO eventSubscription = eventSubscriptionWithSecretTO();
    EventSubscriptionWithIdTO created = create(eventSubscription);

    given()
      .contentType("application/json")
      .delete("/v2/event-subscriptions/" + created.getSubscriptionID())
      .then()
      .assertThat()
      .statusCode(HttpStatus.NO_CONTENT.value())
    ;

    given()
      .contentType("application/json")
      .get("/v2/event-subscriptions/" + created.getSubscriptionID())
      .then()
      .assertThat()
      .statusCode(HttpStatus.NOT_FOUND.value())
    ;
  }

  private static final String callbackUrl = "http://test.me/event-subscription-callback-url";
  private static final String documentReference = "test-docRef";
  private static final String equipmentReference = "test-equipRef";
  private static final String transportCallReference = "test-transportCallRef";
  private static final String vesselIMONumber = "1234567";
  private static final String carrierExportVoyageNumber = "test-carrierExportVoyageNumber";
  private static final String universalExportVoyageReference = "12AZN";
  private static final String carrierServiceCode = "csc34";
  private static final String universalServiceReference = "SR12345Q";
  private static final String UNLocationCode = "UNLoc";
  private static final String secret = "very secret secret";
  private static final Set<EventType> eventTypes = Set.of(EventType.SHIPMENT, EventType.EQUIPMENT);
  private static final Set<ShipmentEventTypeCode> shipmentEventTypeCodes = Set.of(ShipmentEventTypeCode.RECE, ShipmentEventTypeCode.APPR);
  private static final Set<DocumentTypeCode> documentTypeCodes = Set.of(DocumentTypeCode.ARN, DocumentTypeCode.BKG, DocumentTypeCode.CAS);
  private static final Set<TransportEventTypeCode> transportEventTypeCodes = Set.of(TransportEventTypeCode.DEPA);
  private static final Set<EquipmentEventTypeCode> equipmentEventTypeCodes = Set.of(EquipmentEventTypeCode.LOAD, EquipmentEventTypeCode.DISC);

  private EventSubscriptionWithSecretTO eventSubscriptionWithSecretTO() {
    return EventSubscriptionWithSecretTO.builder()
      .callbackUrl(callbackUrl)
      .documentReference(documentReference)
      .equipmentReference(equipmentReference)
      .transportCallReference(transportCallReference)
      .vesselIMONumber(vesselIMONumber)
      .carrierExportVoyageNumber(carrierExportVoyageNumber)
      .universalExportVoyageReference(universalExportVoyageReference)
      .carrierServiceCode(carrierServiceCode)
      .universalServiceReference(universalServiceReference)
      .UNLocationCode(UNLocationCode)
      .eventTypes(eventTypes)
      .shipmentEventTypeCodes(shipmentEventTypeCodes)
      .documentTypeCodes(documentTypeCodes)
      .transportEventTypeCodes(transportEventTypeCodes)
      .equipmentEventTypeCodes(equipmentEventTypeCodes)
      .secret(secret.getBytes(StandardCharsets.UTF_8))
      .build();
  }

  private EventSubscriptionWithIdTO create(EventSubscriptionWithSecretTO eventSubscription) {
    return given()
      .contentType("application/json")
      .body(eventSubscription)
      .post("/v2/event-subscriptions")
      .then()
      .assertThat()
      .statusCode(HttpStatus.CREATED.value())
      .contentType(ContentType.JSON)
      .extract()
      .body()
      .jsonPath()
      .getObject(".", EventSubscriptionWithIdTO.class)
      ;
  }

  private EventSubscriptionWithIdTO retrieve(UUID subscriptionID) {
    return given()
      .contentType("application/json")
      .get("/v2/event-subscriptions/" + subscriptionID)
      .then()
      .assertThat()
      .statusCode(HttpStatus.OK.value())
      .contentType(ContentType.JSON)
      .extract()
      .body()
      .jsonPath()
      .getObject(".", EventSubscriptionWithIdTO.class)
      ;
  }

  private void update(UUID subscriptionID, EventSubscriptionTO eventSubscription) {
    given()
      .contentType("application/json")
      .body(eventSubscription)
      .put("/v2/event-subscriptions/" + subscriptionID)
      .then()
      .assertThat()
      .statusCode(HttpStatus.OK.value())
    ;
  }

  private <T extends Enum<T>> Set<T> inverseSet(Set<T> values, Class<T> enumClass) {
    Set<T> result = new HashSet<>(Set.of(enumClass.getEnumConstants()));
    result.removeAll(values);
    return result;
  }

  private void assertEventSubscriptionTOEquals(EventSubscriptionTO v1, EventSubscriptionTO v2) {
    assertEquals(v1.getCallbackUrl(), v2.getCallbackUrl());
    assertEquals(v1.getDocumentReference(), v2.getDocumentReference());
    assertEquals(v1.getEquipmentReference(), v2.getEquipmentReference());
    assertEquals(v1.getTransportCallReference(), v2.getTransportCallReference());
    assertEquals(v1.getVesselIMONumber(), v2.getVesselIMONumber());
    assertEquals(v1.getCarrierExportVoyageNumber(), v2.getCarrierExportVoyageNumber());
    assertEquals(v1.getUniversalExportVoyageReference(), v2.getUniversalExportVoyageReference());
    assertEquals(v1.getCarrierServiceCode(), v2.getCarrierServiceCode());
    assertEquals(v1.getUniversalServiceReference(), v2.getUniversalServiceReference());
    assertEquals(v1.getUNLocationCode(), v2.getUNLocationCode());
    assertEquals(v1.getEventTypes(), v2.getEventTypes());
    assertEquals(v1.getShipmentEventTypeCodes(), v2.getShipmentEventTypeCodes());
    assertEquals(v1.getDocumentTypeCodes(), v2.getDocumentTypeCodes());
    assertEquals(v1.getTransportEventTypeCodes(), v2.getTransportEventTypeCodes());
    assertEquals(v1.getEquipmentEventTypeCodes(), v2.getEquipmentEventTypeCodes());
  }
}
