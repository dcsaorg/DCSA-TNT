FROM eclipse-temurin:17-jre-alpine
EXPOSE 9090
ENV DB_HOSTNAME db AUTH0_ENABLED true
COPY run-in-container.sh /run.sh
RUN chmod +x /run.sh
COPY src/main/resources/application.yaml .
COPY target/dcsa_tnt-*.jar .
CMD ["/run.sh"]

