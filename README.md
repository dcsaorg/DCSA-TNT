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
Rebuild (reset) the database with test data:
```
docker-compose up -d -V --build
```

