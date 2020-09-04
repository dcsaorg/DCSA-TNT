FROM debian:buster

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get -y upgrade \
    && DEBIAN_FRONTEND=noninteractive apt-get -y install --no-install-recommends \
        openjdk-11-jre-headless \
    && rm -rf /var/lib/apt/lists/*

EXPOSE 9091
ENV db_hostname dcsa_db
COPY target/dcsa_tnt-*.war .
CMD java -jar dcsa_tnt-*.war
