package org.dcsa.tnt.controller;

import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.model.enums.EventClassifierCode;
import org.dcsa.core.events.model.enums.EventType;
import org.dcsa.core.extendedrequest.ExtendedParameters;
import org.dcsa.tnt.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

  @MockBean EventService eventService;

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
}
