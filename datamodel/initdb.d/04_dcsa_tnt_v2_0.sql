-- A script to initialize the tables relevant for the DCSA TNT interface v2.0

\connect dcsa_openapi

DROP TABLE IF EXISTS dcsa_v2_0.event CASCADE;
CREATE TABLE dcsa_v2_0.event (
    event_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    event_type text NOT NULL,
    event_classifier_code varchar(3) NOT NULL,
    event_date_time timestamp with time zone NOT NULL,
    event_type_code varchar(4) NOT NULL
);

DROP TABLE IF EXISTS dcsa_v2_0.equipment_event CASCADE;
CREATE TABLE dcsa_v2_0.equipment_event (
    equipment_reference varchar(15),
    empty_indicator_code text NOT NULL,
    transport_call_id uuid NOT NULL
) INHERITS (dcsa_v2_0.event);

DROP TABLE IF EXISTS dcsa_v2_0.shipment_event CASCADE;
CREATE TABLE dcsa_v2_0.shipment_event (
    shipment_id uuid NOT NULL,
    shipment_information_type_code varchar(3) NOT NULL
) INHERITS (dcsa_v2_0.event);

DROP TABLE IF EXISTS dcsa_v2_0.transport_event CASCADE;
CREATE TABLE dcsa_v2_0.transport_event (
    delay_reason_code varchar(3),
    vessel_schedule_change_remark varchar(250),
    transport_call_id uuid NOT NULL
) INHERITS (dcsa_v2_0.event);

DROP TABLE IF EXISTS dcsa_v2_0.event_subscription CASCADE;
CREATE TABLE dcsa_v2_0.event_subscription (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    callback_url text NOT NULL,
    event_type text, --This field must be able to contain multiple event types. Currently it does not.
    booking_reference varchar(35),
    transport_document_id varchar(20),
    transport_document_type text,
    equipment_reference varchar(15),
    schedule_id uuid NULL,
    transport_call_id uuid NULL
    );


-- Aggregated table containing all events
DROP VIEW IF EXISTS dcsa_v2_0.aggregated_events CASCADE;
CREATE VIEW dcsa_v2_0.aggregated_events AS
 SELECT transport_event.event_id,
    transport_event.event_type,
    transport_event.event_classifier_code,
    transport_event.event_type_code,
    transport_event.event_date_time,
    transport_event.transport_call_id,
    transport_event.delay_reason_code,
    transport_event.vessel_schedule_change_remark,
    NULL::text AS shipment_information_type_code,
    NULL::text AS equipment_reference,
    NULL::text AS empty_indicator_code
   FROM dcsa_v2_0.transport_event
UNION
 SELECT shipment_event.event_id,
    shipment_event.event_type,
    shipment_event.event_classifier_code,
    shipment_event.event_type_code,
    shipment_event.event_date_time,
    NULL::UUID AS transport_call_id,
    NULL::text AS delay_reason_code,
    NULL:: text AS vessel_schedule_change_remark,
    shipment_event.shipment_information_type_code,
    NULL::text AS equipment_reference,
    NULL::text AS empty_indicator_code
   FROM dcsa_v2_0.shipment_event
UNION
 SELECT equipment_event.event_id,
    equipment_event.event_type,
    equipment_event.event_classifier_code,
    equipment_event.event_type_code,
    equipment_event.event_date_time,
    equipment_event.transport_call_id,
    NULL::text AS delay_reason_code,
    NULL:: text AS vessel_schedule_change_remark,
    NULL::text AS shipment_information_type_code,
    equipment_event.equipment_reference,
    equipment_event.empty_indicator_code
   FROM dcsa_v2_0.equipment_event;

--Helper table in order to filter Events on schedule_id
DROP TABLE IF EXISTS dcsa_v2_0.schedule CASCADE;
CREATE TABLE dcsa_v2_0.schedule (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    vessel_operator_carrier_code varchar(10) NOT NULL,
    vessel_operator_carrier_code_list_provider text NOT NULL,
    vessel_partner_carrier_code varchar(10) NOT NULL,
    vessel_partner_carrier_code_list_provider text,
    start_date date,
    date_range interval
);

DROP TABLE IF EXISTS dcsa_v2_0.transport_call CASCADE;
CREATE TABLE dcsa_v2_0.transport_call (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    schedule_id uuid NOT NULL,
    carrier_service_code text,
    vessel_imo_number varchar(7),
    vessel_name varchar(35),
    carrier_voyage_number varchar(50) NOT NULL,
    un_location_code varchar(5) NOT NULL,
    un_location_name varchar(70),
    transport_call_number integer,
    facility_type_code varchar(4) NOT NULL,
    facility_code varchar(11) NOT NULL,
    other_facility varchar(50)
);

-- Helper table in order to filter Events on equipment-reference
DROP TABLE IF EXISTS dcsa_v2_0.shipment CASCADE;
CREATE TABLE dcsa_v2_0.shipment (
    id uuid NOT NULL,
    booking_reference text, -- The identifier for a shipment, which is issued by and unique within each of the carriers.
    booking_datetime timestamp, -- The date and time of the booking request.
    transport_document_id UUID, -- Transport Document ID is an identifier that links to a shipment. Bill of lading is the legal document issued to the customer which confirms the carrier's receipt of the cargo from the customer acknowledging goods being shipped and specifying the terms of delivery.
    transport_document_type_code text,
    shipper_name varchar(50), -- The name of the shipper, who requested the booking
    consignee_name varchar(50), -- The name of the consignee
    collection_origin varchar(250), -- The location through which the shipment originates. It can be defined as a UN Location Code value or an address. The customer (shipper) needs to place a booking in order to ship the cargo (commodity) from an origin to destination. This attribute specifies the location of the origin.
    collection_dateTime timestamp, -- The date and the time that the shipment items need to be collected from the origin.
    delivery_destination varchar(250), -- The location to which the shipment is destined. It can be defined as a UN Location Code value or an address. The customer (shipper) needs to place a booking in order to ship the cargo (commodity) from an origin to destination. This attribute specifies the location of the destination. Also known as 'place of carrier delivery'.
    delivery_datetime timestamp , -- The date (and when possible time) that the shipment items need to be delivered to the destination.
    carrier_code varchar(10) -- The Carrier Code represents a concatenation of the Code List Provider Code and the Code List Provider. A hyphen is used between the two codes. The unique carrier identifier is sourced from either the NMFTA SCAC codes list or the SMDG Master Liner codes list.
    );
