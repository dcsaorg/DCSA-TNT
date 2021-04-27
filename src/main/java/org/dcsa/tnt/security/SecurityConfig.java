package org.dcsa.tnt.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import security.AudienceValidator;
import security.JwtAuthenticationConverter;

/**
 * Configures our application with Spring Security to restrict access to our API endpoints.
 */
@Slf4j
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.enabled:true}")
    private boolean securityEnabled;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        /*
        This is where we configure the security required for our endpoints and setup our app to serve as
        an OAuth2 Resource Server, using JWT validation.
        */

        ServerHttpSecurity.AuthorizeExchangeSpec securitySpec = http.authorizeExchange();

        if (securityEnabled) {
            log.info("Security: auth0 enabled");
            securitySpec
                    .anyExchange().authenticated()
//                .pathMatchers("/*").permitAll()
//                .pathMatchers("/*").authenticated()
//                .pathMatchers("/*").hasAuthority("readwrite:all")
//                .anyExchange().authenticated()
                    .and()
                    .cors()
                    .and()
                    .oauth2ResourceServer()
                    .jwt()
                    .jwtAuthenticationConverter(new JwtAuthenticationConverter());
        } else {
            log.info("Security: disabled - no authentication nor CRSF tokens needed");
            securitySpec.anyExchange().permitAll()
                    .and()
                    .csrf().disable();
        }
        return securitySpec
                .and()
                .build();
    }
}
