-- Database setup script
-- Needs to be executed by user postgres or equivalent

DROP DATABASE IF EXISTS dcsa_openapi;
CREATE DATABASE dcsa_openapi OWNER dcsa_db_owner;
\connect dcsa_openapi
CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- Used to generate UUIDs
CREATE SCHEMA IF NOT EXISTS dcsa_v1_2;
CREATE SCHEMA IF NOT EXISTS dcsa_v2_0;
GRANT ALL PRIVILEGES ON DATABASE dcsa_openapi TO dcsa_db_owner;
GRANT ALL PRIVILEGES ON SCHEMA dcsa_v1_2 TO dcsa_db_owner;
GRANT ALL PRIVILEGES ON SCHEMA dcsa_v2_0 TO dcsa_db_owner;
ALTER DEFAULT PRIVILEGES IN SCHEMA dcsa_v1_2 GRANT ALL ON TABLES TO dcsa_db_owner;
ALTER DEFAULT PRIVILEGES IN SCHEMA dcsa_v2_0 GRANT ALL ON TABLES TO dcsa_db_owner;
