#!/bin/sh

echo "spring.config.import=/application.yml" >> dcsa-spring-boot.properties

if [ -f "/config/dcsa-config.yaml" ]; then
  echo "Add /config/dcsa-config.yaml to dcsa-spring-boot.properties"
  echo "#---" >> dcsa-spring-boot.properties
  echo "spring.config.import=/config/dcsa-config.yaml" >> dcsa-spring-boot.properties
else
  echo "No custom config.  Add a /config/dcsa-config.yaml if you want to set custom properties"
fi

exec java -Dspring.config.location=/dcsa-spring-boot.properties -jar dcsa*.jar
