
-- We don't really use these, but they have to be here since we depend on shared-kernel and it defines them

CREATE TABLE address (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    name varchar(100) NULL,
    street varchar(100) NULL,
    street_number varchar(50) NULL,
    floor varchar(50) NULL,
    postal_code varchar(10) NULL,
    city varchar(65) NULL,
    state_region varchar(65) NULL,
    country varchar(75) NULL
);

CREATE TABLE carrier (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_name varchar(100),
    smdg_code varchar(3) NULL,
    nmfta_code varchar(4) NULL
);

CREATE TABLE country (
    country_code varchar(2) PRIMARY KEY,
    country_name varchar(75) NULL
);

CREATE TABLE un_location (
    un_location_code char(5) PRIMARY KEY,
    un_location_name varchar(100) NULL,
    location_code char(3) NULL,
    country_code char(2) NULL REFERENCES country (country_code)
);

CREATE TABLE location (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    location_name varchar(100) NULL,
    latitude varchar(10) NULL,
    longitude varchar(11) NULL,
    un_location_code char(5) NULL REFERENCES un_location (un_location_code),
    address_id uuid NULL REFERENCES address (id),
    facility_id uuid NULL  -- REFERENCES facility (but there is a circular relation, so we add the FK later)
);

CREATE TABLE facility_type (
    facility_type_code varchar(4) PRIMARY KEY,
    facility_type_name varchar(100) NOT NULL,
    facility_type_description varchar(250) NOT NULL
);

CREATE TABLE facility (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    facility_name varchar(100) NULL,
    un_location_code varchar(5) NULL REFERENCES un_location (un_location_code), -- The UN Locode prefixing the BIC / SMDG code
    facility_bic_code varchar(4) NULL, -- suffix uniquely identifying the facility when prefixed with the UN Locode
    facility_smdg_code varchar(6) NULL, -- suffix uniquely identifying the facility when prefixed with the UN Locode
    location_id uuid REFERENCES location(id)
);
ALTER TABLE location
    ADD FOREIGN KEY (facility_id) REFERENCES facility (id);

