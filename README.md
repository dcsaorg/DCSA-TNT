# DCSA TNT 

Building and running manually/locally
-------------------------------------

**Note:** If you have your Github Personal Access Token setup as mentioned [here](https://github.com/dcsaorg/DCSA-Core/blob/master/README.md#how-to-use-dcsa-core-packages), skip to step 3.

1) Build **DCSA-Core** as described in [DCSA-Core/README.md](https://github.com/dcsaorg/DCSA-Core/blob/master/README.md#to-build-manually-run), then

2) Build **DCSA-Event-Core** as described in [DCSA-Event-Core/README.md](https://github.com/dcsaorg/DCSA-Event-Core/blob/master/README.md#to-build-manually-run), then

3) Clone **DCSA-TNT** (with ``--recurse-submodules`` option.) and Build using, ``mvn package``

4) Initialize your local postgresql database as described in [datamodel/README.md](https://github.com/dcsaorg/DCSA-Information-Model/blob/master/README.md)

5) Run application,
```
mvn spring-boot:run [options] 

options:
 -Dspring-boot.run.arguments="--DB_HOSTNAME=localhost:5432 --AUTH0_ENABLED=false --LOG_LEVEL=DEBUG"
```

OR using **docker-compose**

```
docker-compose up -d -V --build
```

Check the running application,
```
curl http://localhost:9090/v2/actuator/health
```
