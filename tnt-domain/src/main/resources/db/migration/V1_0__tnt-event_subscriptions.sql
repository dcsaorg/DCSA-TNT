
CREATE TABLE tnt_event_data (
    event_id varchar(100) NOT NULL PRIMARY KEY,
    event_type varchar(16) NOT NULL,
    content jsonb NOT NULL,
    event_created_date_time timestamp with time zone NOT NULL,
    event_date_time timestamp with time zone NULL
);
CREATE INDEX ON tnt_event_data (event_type);
CREATE INDEX ON tnt_event_data (event_created_date_time);
CREATE INDEX ON tnt_event_data (event_date_time);
CREATE INDEX ON tnt_event_data USING btree ((content->>'transportEventTypeCode'));
CREATE INDEX ON tnt_event_data USING btree ((content->>'equipmentEventTypeCode'));
CREATE INDEX ON tnt_event_data USING btree ((content->>'shipmentEventTypeCode'));
CREATE INDEX ON tnt_event_data USING btree ((content->>'documentTypeCode'));
CREATE INDEX ON tnt_event_data USING btree ((content->'transportCall'->'vessel'->>'vesselIMONumber'));
CREATE INDEX ON tnt_event_data USING btree ((content->'transportCall'->>'transportCallReference'));
CREATE INDEX ON tnt_event_data USING btree ((content->'transportCall'->>'carrierServiceCode'));
CREATE INDEX ON tnt_event_data USING btree ((content->'transportCall'->>'carrierImportVoyageNumber'));
CREATE INDEX ON tnt_event_data USING btree ((content->'transportCall'->>'carrierExportVoyageNumber'));
CREATE INDEX ON tnt_event_data USING btree ((content->'transportCall'->>'universalExportVoyageReference'));
CREATE INDEX ON tnt_event_data USING btree ((content->'transportCall'->>'universalServiceReference'));
CREATE INDEX ON tnt_event_data USING btree ((content->'transportCall'->'location'->>'UNLocationCode'));
CREATE INDEX ON tnt_event_data USING btree ((content->>'equipmentReference'));

-- Helper view to make queries more readable
CREATE VIEW tnt_event AS
  SELECT
    event_id,
    event_type,
    content,
    event_created_date_time,
    event_date_time,
    content->>'transportEventTypeCode' transport_event_type_code,
    content->>'equipmentEventTypeCode' equipment_event_type_code,
    content->>'shipmentEventTypeCode' shipment_event_type_code,
    content->>'documentTypeCode' document_type_code,
    content->'transportCall'->'vessel'->>'vesselIMONumber' vessel_imo_number,
    content->'transportCall'->>'transportCallReference' transport_call_reference,
    content->'transportCall'->>'carrierServiceCode' carrier_service_code,
    content->'transportCall'->>'carrierImportVoyageNumber' carrier_import_voyage_number,
    content->'transportCall'->>'carrierExportVoyageNumber' carrier_export_voyage_number,
    content->'transportCall'->>'universalExportVoyageReference' universal_export_voyage_reference,
    content->'transportCall'->>'universalServiceReference' universal_service_reference,
    content->'transportCall'->'location'->>'UNLocationCode' un_location_code,
    content->>'equipmentReference' equipment_reference
  FROM tnt_event_data;

-- allows inserts on the view so we only need one entity class
CREATE RULE tnt_event_insert AS ON INSERT TO tnt_event DO INSTEAD
  INSERT INTO tnt_event_data (event_id, event_type, content, event_created_date_time, event_date_time)
    VALUES (NEW.event_id, NEW.event_type, NEW.content, NEW.event_created_date_time, NEW.event_date_time);


CREATE TABLE tnt_event_document_reference (
    id uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    event_id varchar(100) NOT NULL REFERENCES tnt_event_data (event_id),
    type varchar(3) NOT NULL,
    reference varchar(100) NOT NULL
);
CREATE UNIQUE INDEX ON tnt_event_document_reference (event_id, type, reference);

CREATE TABLE tnt_event_reference (
    id uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    event_id varchar(100) NOT NULL REFERENCES tnt_event_data (event_id),
    type varchar(3) NOT NULL,
    reference varchar(100) NOT NULL
);
CREATE UNIQUE INDEX ON tnt_event_reference (event_id, type, reference);


CREATE TABLE event_subscription (
    subscription_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    callback_url text NOT NULL,
    document_reference varchar(100) NULL,
    equipment_reference varchar(15) NULL,
    transport_call_reference varchar(100) NULL,
    vessel_imo_number varchar(7) NULL,
    carrier_export_voyage_number varchar(50) NULL,
    universal_export_voyage_reference varchar(5) NULL,
    carrier_service_code varchar(11) NULL,
    universal_service_reference varchar(8) NULL,
    un_location_code varchar(5) NULL,
    secret bytea NOT NULL,
    created_date_time timestamp with time zone NOT NULL default now()
);
CREATE INDEX ON event_subscription (created_date_time);

CREATE TABLE event_subscription_event_type (
    subscription_id uuid NOT NULL REFERENCES event_subscription (subscription_id) ON DELETE CASCADE,
    event_type varchar(16) NOT NULL,
    PRIMARY KEY (subscription_id, event_type)
);

CREATE TABLE event_subscription_transport_event_type_code (
    subscription_id uuid NOT NULL REFERENCES event_subscription (subscription_id) ON DELETE CASCADE,
    transport_event_type_code varchar(4) NOT NULL,
    PRIMARY KEY (subscription_id, transport_event_type_code)
);

CREATE TABLE event_subscription_shipment_event_type_code (
    subscription_id uuid NOT NULL REFERENCES event_subscription (subscription_id) ON DELETE CASCADE,
    shipment_event_type_code varchar(4) NOT NULL,
    PRIMARY KEY (subscription_id, shipment_event_type_code)
);

CREATE TABLE event_subscription_equipment_event_type_code (
    subscription_id uuid NOT NULL REFERENCES event_subscription (subscription_id) ON DELETE CASCADE,
    equipment_event_type_code varchar(4) NOT NULL,
    PRIMARY KEY (subscription_id, equipment_event_type_code)
);

CREATE TABLE event_subscription_document_type_code (
    subscription_id uuid NOT NULL REFERENCES event_subscription (subscription_id) ON DELETE CASCADE,
    document_type_code varchar(4) NOT NULL,
    PRIMARY KEY (subscription_id, document_type_code)
);
