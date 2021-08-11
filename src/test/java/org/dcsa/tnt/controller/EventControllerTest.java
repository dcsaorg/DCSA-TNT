package org.dcsa.tnt.controller;

import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.model.enums.EventClassifierCode;
import org.dcsa.core.events.model.enums.EventType;
import org.dcsa.core.events.service.GenericEventService;
import org.dcsa.core.extendedrequest.ExtendedParameters;
import org.dcsa.tnt.service.TNTEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.OffsetDateTime;
import java.util.UUID;

@DisplayName("Tests for Event Controller")
@WebFluxTest(controllers = EventController.class)
class EventControllerTest {

  @Autowired WebTestClient webTestClient;

  @MockBean
  @Qualifier("TNTEventServiceImpl") TNTEventService eventService;

  @MockBean ExtendedParameters extendedParameters;

  @MockBean R2dbcDialect r2dbcDialect;

  private Event event;

  @TestConfiguration
  @EnableWebFluxSecurity
  static class WebFluxSecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
      return http.csrf().disable().build();
    }
  }

  @BeforeEach
  void init() {
    event = new Event();
    event.setEventID(UUID.randomUUID());
    event.setEventType(EventType.EQUIPMENT);
    event.setEventClassifierCode(EventClassifierCode.ACT);
    event.setEventDateTime(OffsetDateTime.now());
    event.setEventCreatedDateTime(OffsetDateTime.now());
  }

  @Test
  @DisplayName("Creation of an event should throw forbidden for any valid request.")
  void eventCreationShouldThrowForbiddenForAnyRequest() {
    // test to confirm that the endpoint is disabled.
    webTestClient
        .post()
        .uri("/events")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(event))
        .exchange()
        .expectStatus()
        .isForbidden();
  }

  @Test
  @DisplayName("Updating an event should throw forbidden for any valid request.")
  void eventUpdatingShouldThrowForbiddenForAnyRequest() {
    // test to confirm that the endpoint is disabled.
    webTestClient
        .put()
        .uri("/events/{id}", event.getEventID())
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(event))
        .exchange()
        .expectStatus()
        .isForbidden();
  }

  @Test
  @DisplayName("Deleting an event should throw forbidden for any valid request.")
  void eventDeletingShouldThrowForbiddenForAnyRequest() {
    // test to confirm that the endpoint is disabled.
    webTestClient
        .delete()
        .uri("/events/{id}", event.getEventID())
        .exchange()
        .expectStatus()
        .isForbidden();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect eventType format.")
  void testEventsShouldFailForIncorrectEventType() {
    webTestClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/events").queryParam("eventType", "ABCD,DUMMY").build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect shipmentEventTypeCode format.")
  void testEventsShouldFailForIncorrectShipmentEventTypeCode() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/events")
                    .queryParam("shipmentEventTypeCode", "ABCD,DUMMY")
                    .build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect carrierBookingReference format.")
  void testEventsShouldFailForIncorrectCarrierBookingReference() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/events")
                    .queryParam(
                        "carrierBookingReference", "ABC709951ABC709951ABC709951ABC709951564")
                    .build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName(
      "Get events should throw bad request for incorrect transportDocumentReference format.")
  void testEventsShouldFailForIncorrectTransportDocumentReference() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/events")
                    .queryParam(
                        "transportDocumentReference", "ABC709951ABC709951ABC709951ABC709951564")
                    .build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName(
      "Get events should throw bad request for incorrect transportDocumentTypeCode format.")
  void testEventsShouldFailForIncorrectTransportDocumentTypeCode() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/events").queryParam("transportDocumentTypeCode", "DUMMY").build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect transportEventTypeCode format.")
  void testEventsShouldFailForIncorrectTransportEventTypeCode() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/events").queryParam("transportEventTypeCode", "DUMMY").build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect transportCallID format.")
  void testEventsShouldFailForIncorrectTransportCallID() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/events")
                    .queryParam(
                        "transportCallID",
                        "IGAcb79ijzjpLT4eicqw70C5X2lN591BhprgTwAkMeaRehoZ6OVcvMYGl0Hyb35jR2tWrWafyUBo89dIXRd1MVJkYkdqleU6XbBxY")
                    .build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect vesselIMONumber format.")
  void testEventsShouldFailForIncorrectVesselIMONumber() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/events").queryParam("vesselIMONumber", "ABsC70sss99").build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect carrierVoyageNumber format.")
  void testEventsShouldFailForIncorrectCarrierVoyageNumber() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/events")
                    .queryParam(
                        "carrierVoyageNumber",
                        "pZwVm6KNaM6VWTty7yqNEQvB5pf8ElUCzdlu2kWWy4QCMpOnXU3")
                    .build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect carrierServiceCode format.")
  void testEventsShouldFailForIncorrectCarrierServiceCode() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/events").queryParam("carrierServiceCode", "ABsC70sss99").build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect equipmentEventTypeCode format.")
  void testEventsShouldFailForIncorrectEquipmentEventTypeCode() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/events").queryParam("equipmentEventTypeCode", "DUMMY").build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request for incorrect equipmentReference format.")
  void testEventsShouldFailForIncorrectEquipmentReference() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/events")
                    .queryParam("equipmentReference", "dsfdsAPZU4812090APZU4812090APZU4812090")
                    .build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @DisplayName("Get events should throw bad request if limit is zero.")
  void testEventsShouldFailForIncorrectLimit() {
    webTestClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/events").queryParam("limit", 0).build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }
}
