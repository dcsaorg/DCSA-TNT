FROM debian:buster

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get -y upgrade \
    && DEBIAN_FRONTEND=noninteractive apt-get -y install --no-install-recommends \
        openjdk-11-jre-headless \
    && rm -rf /var/lib/apt/lists/*

EXPOSE 9090
ENV DB_HOSTNAME db AUTH0_ENABLED true
COPY run-in-container.sh /run.sh
RUN chmod +x /run.sh
COPY src/main/resources/application.yaml .
COPY target/dcsa_tnt-*.jar .
CMD ["/run.sh"]

