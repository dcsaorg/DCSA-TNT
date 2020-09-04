-- A script to initialize the tables relevant for the DCSA TNT interface v2.0

\connect dcsa_openapi

DROP TYPE IF EXISTS dcsa_v2_0.CarrierCodeListProvider CASCADE;
CREATE TYPE dcsa_v2_0.CarrierCodeListProvider AS ENUM (
    'SMDG',
    'NMFTA'
);

DROP TYPE IF EXISTS dcsa_v2_0.EmptyIndicatorCode CASCADE;
CREATE TYPE dcsa_v2_0.EmptyIndicatorCode AS ENUM (
    'EMPTY',
    'LADEN'
);

DROP TYPE IF EXISTS dcsa_v2_0.EventClassifierCode CASCADE;
CREATE TYPE dcsa_v2_0.EventClassifierCode AS ENUM (
    'PLN',
    'ACT',
    'EST'
);

DROP TYPE IF EXISTS dcsa_v2_0.EventType CASCADE;
CREATE TYPE dcsa_v2_0.EventType AS ENUM (
    'TRANSPORT',
    'SHIPMENT',
    'EQUIPMENT',
    'TRANSPORTEQUIPMENT'
);

DROP TYPE IF EXISTS dcsa_v2_0.EventTypeCode CASCADE;
CREATE TYPE dcsa_v2_0.EventTypeCode AS ENUM (
    'ARRI',
    'DEPA'
);

DROP TABLE IF EXISTS dcsa_v2_0.equipment_event CASCADE;
CREATE TABLE dcsa_v2_0.equipment_event (
    id uuid NOT NULL,
    event_type dcsa_v2_0.EventType NOT NULL,
    event_date_time timestamp with time zone NOT NULL,
    event_classifier_code dcsa_v2_0.EventClassifierCode NOT NULL,
    equipment_reference character varying(15),
    facility_type_code character varying(4) NOT NULL,
    un_location_code character varying(5) NOT NULL,
    facility_code character varying(11) NOT NULL,
    other_facility character varying(50),
    empty_indicator_code dcsa_v2_0.EmptyIndicatorCode NOT NULL,
    event_type_code dcsa_v2_0.EventTypeCode NOT NULL,
    transport_call_id uuid NOT NULL
);

DROP TABLE IF EXISTS dcsa_v2_0.schedule CASCADE;
CREATE TABLE dcsa_v2_0.schedule (
    id uuid NOT NULL,
    vessel_operator_carrier_code character varying(10) NOT NULL,
    vessel_partner_carrier_code character varying(10),
    start_date date,
    date_range interval,
    vessel_operator_carrier_code_list_provider dcsa_v2_0.CarrierCodeListProvider NOT NULL,
    vessel_partner_carrier_code_list_provider dcsa_v2_0.CarrierCodeListProvider
);

DROP TABLE IF EXISTS dcsa_v2_0.shipment_event CASCADE;
CREATE TABLE dcsa_v2_0.shipment_event (
    id uuid NOT NULL,
    shipment_id uuid NOT NULL,
    event_type dcsa_v2_0.EventType NOT NULL,
    event_date_time timestamp with time zone NOT NULL,
    event_classifier_code dcsa_v2_0.EventClassifierCode NOT NULL,
    shipment_information_type_code character varying(3) NOT NULL,
    event_type_code dcsa_v2_0.EventTypeCode NOT NULL
);

DROP TABLE IF EXISTS dcsa_v2_0.transport_call CASCADE;
CREATE TABLE dcsa_v2_0.transport_call (
    id uuid NOT NULL,
    schedule_id uuid NOT NULL,
    carrier_service_code character varying,
    vessel_imo_number character varying(7),
    vessel_name character varying(35),
    carrier_voyage_number character varying(50) NOT NULL,
    un_location_code character varying(5) NOT NULL,
    un_location_name character varying(70),
    transport_call_number integer,
    facility_type_code character varying(4) NOT NULL,
    facility_code character varying(11) NOT NULL,
    other_facility character varying(50)
);

DROP TABLE IF EXISTS dcsa_v2_0.transport_equipment_event CASCADE;
CREATE TABLE dcsa_v2_0.transport_equipment_event (
    id uuid NOT NULL,
    event_type dcsa_v2_0.EventType NOT NULL,
    event_date_time timestamp with time zone NOT NULL,
    event_classifier_code dcsa_v2_0.EventClassifierCode NOT NULL,
    transport_reference character varying(50) NOT NULL,
    transport_leg_reference character varying NOT NULL,
    equipment_reference character varying(15),
    facility_type_code character varying(4) NOT NULL,
    un_location_code character varying(5) NOT NULL,
    facility_code character varying(11) NOT NULL,
    other_facility character varying(50),
    empty_indicator_code dcsa_v2_0.EmptyIndicatorCode NOT NULL,
    mode_of_transport_code integer,
    event_type_code dcsa_v2_0.EventTypeCode NOT NULL
);

DROP TABLE IF EXISTS dcsa_v2_0.transport_event CASCADE;
CREATE TABLE dcsa_v2_0.transport_event (
    id uuid NOT NULL,
    event_type dcsa_v2_0.EventType NOT NULL,
    event_date_time timestamp with time zone NOT NULL,
    transport_call_id uuid NOT NULL,
    delay_reason_code character varying(3),
    vessel_schedule_change_remark character varying(250),
    event_classifier_code dcsa_v2_0.EventClassifierCode NOT NULL,
    event_type_code dcsa_v2_0.EventTypeCode NOT NULL
);

DROP TABLE IF EXISTS dcsa_v2_0.transport_document_type CASCADE;
CREATE TABLE dcsa_v2_0.transport_document_type (
    transport_document_type_code integer NOT NULL, -- The type of the transport document (i.e. BOL (bill of lading) or SWB (Sea Waybill).
    transport_document_type_name varchar(50), -- The full names of the document types (Bill of Lading or Sea Waybill).
    transport_document_type_description text -- A description of the different docuemtn types.
);

DROP TYPE IF EXISTS dcsa_v2_0.transport_document_type_code CASCADE;
CREATE TYPE dcsa_v2_0.transport_document_type_code AS ENUM (
    'BOL', -- Bill of Lading
    'SWB' -- Sea Waybill
);

DROP TABLE IF EXISTS dcsa_v2_0.shipment CASCADE;
CREATE TABLE dcsa_v2_0.shipment (
    shipment_id uuid NOT NULL,
    booking_reference text, -- The identifier for a shipment, which is issued by and unique within each of the carriers.
    booking_datetime timestamp, -- The date and time of the booking request.
    transport_document_id varchar(20), -- Transport Document ID is an identifier that links to a shipment. Bill of lading is the legal document issued to the customer which confirms the carrier's receipt of the cargo from the customer acknowledging goods being shipped and specifying the terms of delivery.
    transport_document_type_code dcsa_v2_0.transport_document_type_code,
    shipper_name varchar(50), -- The name of the shipper, who requested the booking
    consignee_name varchar(50), -- The name of the consignee
    collection_origin varchar(250), -- The location through which the shipment originates. It can be defined as a UN Location Code value or an address. The customer (shipper) needs to place a booking in order to ship the cargo (commodity) from an origin to destination. This attribute specifies the location of the origin.
    collection_dateTime timestamp, -- The date and the time that the shipment items need to be collected from the origin.
    delivery_destination varchar(250), -- The location to which the shipment is destined. It can be defined as a UN Location Code value or an address. The customer (shipper) needs to place a booking in order to ship the cargo (commodity) from an origin to destination. This attribute specifies the location of the destination. Also known as 'place of carrier delivery'.
    delivery_datetime timestamp , -- The date (and when possible time) that the shipment items need to be delivered to the destination.
    carrier_code varchar(10) -- The Carrier Code represents a concatenation of the Code List Provider Code and the Code List Provider. A hyphen is used between the two codes. The unique carrier identifier is sourced from either the NMFTA SCAC codes list or the SMDG Master Liner codes list.
    );

ALTER TABLE ONLY dcsa_v2_0.equipment_event
    ADD CONSTRAINT equipment_event_pkey PRIMARY KEY (id);

ALTER TABLE ONLY dcsa_v2_0.schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (id);

ALTER TABLE ONLY dcsa_v2_0.transport_call
    ADD CONSTRAINT transport_call_pkey PRIMARY KEY (id);

ALTER TABLE ONLY dcsa_v2_0.transport_equipment_event
    ADD CONSTRAINT transport_equipment_event_pkey PRIMARY KEY (id);

ALTER TABLE ONLY dcsa_v2_0.transport_event
    ADD CONSTRAINT transport_event_pkey PRIMARY KEY (id);

ALTER TABLE ONLY dcsa_v2_0.equipment_event
    ADD CONSTRAINT equipment_event_transport_call_id_fkey FOREIGN KEY (transport_call_id) REFERENCES dcsa_v2_0.transport_call(id);

ALTER TABLE ONLY dcsa_v2_0.transport_call
    ADD CONSTRAINT transport_call_schedule_id_fkey FOREIGN KEY (schedule_id) REFERENCES dcsa_v2_0.schedule(id);

ALTER TABLE ONLY dcsa_v2_0.transport_event
    ADD CONSTRAINT transport_event_transport_call_id_fkey FOREIGN KEY (transport_call_id) REFERENCES dcsa_v2_0.transport_call(id);
