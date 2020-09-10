# DCSA Backend

Building and running manually
-----------------------------

Initialize your local postgresql database as described in datamodel/README.md, then
```
export db_hostname=localhost
mvn spring-boot:run
```

Building and running using docker-compose
-----------------------------------------
To build using DCSA-core from GitHub packages
```
mvn package
docker-compose up -d -V --build
```

To build using locally built DCSA-core
NOTE: the "mvn install" command's "-DFile" parameter should point to a compiled dcsa_core .jar file
```
mvn install:install-file -Dfile=../DCSA-Core/target/dcsa_core-0.3.0.jar -DgroupId=org.dcsa -DartifactId=dcsa_core -Dversion=local-SNAPSHOT -Dpackaging=jar -DgeneratePom=true

mvn package -Ddcsa.version=local-SNAPSHOT

docker-compose up -d -V --build
´´´
