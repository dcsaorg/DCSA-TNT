package org.dcsa.tnt.config;

import org.dcsa.core.repository.ExtendedRepositoryImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("org.dcsa")
@EnableR2dbcRepositories(
    basePackages = {"org.dcsa"},
    repositoryBaseClass = ExtendedRepositoryImpl.class)

@EnableScheduling
public class ApplicationConfig {}
