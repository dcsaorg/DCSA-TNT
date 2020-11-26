# DCSA TNT 

Building and running manually/locally
-------------------------------------

Initialize your local postgresql database as described in datamodel/README.md, then
```
export db_hostname=localhost
export DCSA_Version=0.7.4 #or whatever version is the right one
```
If running without auth0, disable it in src/main/resources/application.yaml

Then build and run with
```
mvn install:install-file -Dfile=../DCSA-Core/target/dcsa_core-$DCSA_Version.jar -DgroupId=org.dcsa -DartifactId=dcsa_core -Dversion=local-SNAPSHOT -Dpackaging=jar -DgeneratePom=true
mvn spring-boot:run -Ddcsa.version=local-SNAPSHOT

```
or using docker-compose

```
mvn package -Ddcsa.version=local-SNAPSHOT

docker-compose up -d -V --build
´´´

Building and running using docker-compose
-----------------------------------------
To build using DCSA-core from GitHub packages
```
mvn package
docker-compose up -d -V --build
```
