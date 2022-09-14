package org.dcsa.tnt.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class JacksonConfiguration {
  @Bean
  public ObjectMapper defaultObjectMapper() {
    return new ObjectMapper()
      .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
      .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .findAndRegisterModules();
  }
}
