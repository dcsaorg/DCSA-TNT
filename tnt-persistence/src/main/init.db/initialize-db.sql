-- Database setup script
-- Needs to be executed by user postgres or equivalent

\set ON_ERROR_STOP true

CREATE USER dcsa_db_owner WITH PASSWORD '9c072fe8-c59c-11ea-b8d1-7b6577e9f3f5';
CREATE DATABASE dcsa_tnt OWNER dcsa_db_owner;

GRANT ALL PRIVILEGES ON DATABASE dcsa_tnt TO dcsa_db_owner;

