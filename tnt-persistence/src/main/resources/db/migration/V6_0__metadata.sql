
-- Metadata IDs

-- Most of the R2DBC tooling we are currently using requires that every entity
-- has a direct ID.  This change is to insert these, so the code works but they
-- are not a part of the original data model.

ALTER TABLE shipment_location
    ADD COLUMN IF NOT EXISTS id uuid DEFAULT uuid_generate_v4() PRIMARY KEY;

ALTER TABLE cargo_line_item
    ADD COLUMN IF NOT EXISTS id uuid DEFAULT uuid_generate_v4() PRIMARY KEY;

ALTER TABLE shipment_transport
    ADD COLUMN IF NOT EXISTS id uuid DEFAULT uuid_generate_v4() PRIMARY KEY;

ALTER TABLE seal
    ADD COLUMN IF NOT EXISTS id uuid DEFAULT uuid_generate_v4() PRIMARY KEY;

ALTER TABLE reference
    ADD COLUMN IF NOT EXISTS id uuid DEFAULT uuid_generate_v4() PRIMARY KEY;

ALTER TABLE party_identifying_code
    ADD COLUMN IF NOT EXISTS id uuid DEFAULT uuid_generate_v4() PRIMARY KEY;

ALTER TABLE value_added_service_request
    ADD COLUMN IF NOT EXISTS id uuid DEFAULT uuid_generate_v4() PRIMARY KEY;

-- DateTime metadata

-- Metadata for Booking table to avoid having to query shipmentEvent for
-- updated date_time necessary for BookingResponseTO

ALTER TABLE booking
    ADD COLUMN IF NOT EXISTS updated_date_time timestamp with time zone NOT NULL;

ALTER TABLE shipping_instruction
    ADD COLUMN IF NOT EXISTS created_date_time timestamp with time zone NOT NULL;

ALTER TABLE shipping_instruction
    ADD COLUMN IF NOT EXISTS updated_date_time timestamp with time zone NOT NULL;

ALTER TABLE transport_document
    ADD COLUMN IF NOT EXISTS created_date_time timestamp with time zone NOT NULL;

ALTER TABLE transport_document
    ADD COLUMN IF NOT EXISTS updated_date_time timestamp with time zone NOT NULL;

ALTER TABLE shipment
    ADD COLUMN IF NOT EXISTS updated_date_time timestamp with time zone NOT NULL;

