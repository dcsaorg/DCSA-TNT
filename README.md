# DCSA TNT - Track & Trace

Building and Running the project,
-------------------------------------
**[RECOMMENDED]**
Setup a Github Personal Access Token as mentioned [here](https://github.com/dcsaorg/DCSA-Core/blob/master/README.md#how-to-use-dcsa-core-packages), then skip to **step 3**.

If you would like to build required DCSA packages individually, begin with step 1.

1) Build **DCSA-Core** as described in [DCSA-Core/README.md](https://github.com/dcsaorg/DCSA-Core/blob/master/README.md#to-build-manually-run), then

2) Build **DCSA-Event-Core** as described in [DCSA-Event-Core/README.md](https://github.com/dcsaorg/DCSA-Event-Core/blob/master/README.md#to-build-manually-run), then

3) Clone **DCSA-TNT** (with ``--recurse-submodules`` option.) and Build using, ``mvn package``

4) Initialize your local postgresql database as described in [datamodel/README.md](https://github.com/dcsaorg/DCSA-Information-Model/blob/master/README.md) \
   or If you have docker installed, you may skip this step and use the docker-compose command mentioned below to set it up (This will initialize the application along with the database).

5) Run application,
```
mvn spring-boot:run [options]

options:
 -Dspring-boot.run.arguments="--DB_HOSTNAME=localhost:5432 --LOG_LEVEL=DEBUG"
 ```

OR using **docker-compose**

```
docker-compose up -d -V --build
```

6) Verify if the application is running,
```
curl http://localhost:9090/v1/actuator/health
```

## Security considerations

This reference implementation does not do any authentication/authorization and should not be used
in production as is. Using this as is in production would expose data for all parties to all other
parties with out checking weather they should have access.
