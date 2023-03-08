
CREATE TABLE shipment_event_type (
    shipment_event_type_code varchar(4) PRIMARY KEY,
    shipment_event_type_name varchar(30) NOT NULL,
    shipment_event_type_description varchar(350) NOT NULL
);

CREATE TABLE unit_of_measure (
    unit_of_measure_code varchar(3) PRIMARY KEY,
    unit_of_measure_description varchar(50) NOT NULL
);

CREATE TABLE hs_code (
    hs_code varchar(10) PRIMARY KEY,
    hs_code_description varchar(250) NOT NULL
);

CREATE TABLE value_added_service_code (
    value_added_service_code varchar(5) PRIMARY KEY,
    value_added_service_name varchar(100) NOT NULL,
    value_added_service_description varchar(200) NOT NULL
);

CREATE TABLE reference_type (
    reference_type_code varchar(3) PRIMARY KEY,
    reference_type_name varchar(100) NOT NULL,
    reference_type_description varchar(400) NOT NULL
);

CREATE TABLE receipt_delivery_type (
    receipt_delivery_type_code varchar(3) PRIMARY KEY,
    receipt_delivery_type_name varchar(50) NOT NULL,
    receipt_delivery_type_description varchar(300) NOT NULL
);

CREATE TABLE cargo_movement_type (
    cargo_movement_type_code varchar(3) PRIMARY KEY,
    cargo_movement_type_name varchar(50) NOT NULL,
    cargo_movement_type_description varchar(300) NOT NULL
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

CREATE TABLE carrier (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_name varchar(100),
    smdg_code varchar(3) NULL,
    nmfta_code varchar(4) NULL
);

CREATE TABLE party (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    party_name varchar(100) NULL,
    tax_reference_1 varchar(20) NULL,
    tax_reference_2 varchar(20) NULL,
    public_key varchar(500) NULL,
    address_id uuid NULL REFERENCES address (id)
);

CREATE TABLE party_contact_details (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    party_id uuid NOT NULL REFERENCES party(id),
    name varchar(100) NOT NULL,
    email varchar(100) NULL,
    phone varchar(30) NULL,
    url varchar(100) NULL
);

CREATE TABLE code_list_responsible_agency (
    dcsa_responsible_agency_code varchar(5) NOT NULL PRIMARY KEY,
    code_list_responsible_agency_code varchar(3) NULL,
    code_list_responsible_agency_name varchar(100) NULL,
    code_list_responsible_agency_description varchar(300)
);

CREATE TABLE party_identifying_code (
    dcsa_responsible_agency_code varchar(5) NOT NULL REFERENCES code_list_responsible_agency(dcsa_responsible_agency_code),
    party_id uuid NOT NULL REFERENCES party(id),
    code_list_name varchar(100),
    party_code varchar(100) NOT NULL
);

CREATE TABLE payment_term_type (
    payment_term_code varchar(3) PRIMARY KEY,
    payment_term_name varchar(100) NOT NULL,
    payment_term_description varchar(250) NOT NULL
);

CREATE TABLE incoterms (
    incoterms_code varchar(3) PRIMARY KEY,
    incoterms_name varchar(100) NOT NULL,
    incoterms_description varchar(250) NOT NULL
);

CREATE TABLE transport_document_type (
    transport_document_type_code varchar(3) PRIMARY KEY,
    transport_document_type_name varchar(20) NULL,
    transport_document_type_description varchar(500) NULL
);

CREATE TABLE cut_off_time (
    cut_off_time_code varchar(3) PRIMARY KEY,
    cut_off_time_name varchar(100) NULL,
    cut_off_time_description varchar(250) NULL
);

CREATE TABLE vessel_type (
    vessel_type_code varchar(4) PRIMARY KEY,
    vessel_type_name varchar(100) NULL,
    unece_concatenated_means_of_transport_code varchar(4),
    vessel_type_description varchar(100) NOT NULL
);

CREATE TABLE vessel (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    vessel_imo_number varchar(7) NULL UNIQUE,
    vessel_name varchar(35) NULL,
    vessel_flag char(2) NULL,
    vessel_call_sign varchar(10) NULL,
    vessel_operator_carrier_id uuid NULL REFERENCES carrier (id),
    is_dummy boolean NOT NULL default false,
    length_overall numeric NULL,
    width numeric NULL,
    vessel_type_code varchar(4) NULL REFERENCES vessel_type (vessel_type_code),
    dimension_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CONSTRAINT dimension_unit CHECK (dimension_unit IN ('FOT','MTR'))
);

CREATE TABLE communication_channel_qualifier (
    communication_channel_qualifier_code varchar(2) PRIMARY KEY,
    communication_channel_qualifier_name varchar(100) NOT NULL,
    communication_channel_qualifier_description varchar(250) NOT NULL
);

CREATE TABLE vessel_sharing_agreement_type (
    vessel_sharing_agreement_type_code varchar(3) NOT NULL PRIMARY KEY,
    vessel_sharing_agreement_type_name varchar(50) NULL,
    vessel_sharing_agreement_type_description varchar(250) NOT NULL
);

CREATE TABLE vessel_sharing_agreement (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    vessel_sharing_agreement_name varchar(50) NULL,
    vessel_sharing_agreement_type_code varchar(3) NOT NULL REFERENCES vessel_sharing_agreement_type(vessel_sharing_agreement_type_code)
);

CREATE TABLE tradelane (
    id varchar(8) PRIMARY KEY,
    tradelane_name varchar(150) NOT NULL,
    vessel_sharing_agreement_id uuid NOT NULL REFERENCES vessel_sharing_agreement(id)
);

CREATE TABLE service (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_id uuid NULL REFERENCES carrier (id),
    carrier_service_code varchar(11),
    carrier_service_name varchar(50),
    tradelane_id varchar(8) NULL REFERENCES tradelane(id),
    universal_service_reference varchar(8) NULL CHECK (universal_service_reference ~ '^SR\d{5}[A-Z]$')
);

CREATE TABLE voyage (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_voyage_number varchar(50) NOT NULL,
    universal_voyage_reference varchar(5) NULL,
    service_id uuid NULL REFERENCES service (id) INITIALLY DEFERRED
);

CREATE TABLE mode_of_transport (
    mode_of_transport_code varchar(3) PRIMARY KEY,
    mode_of_transport_name varchar(100) NULL,
    mode_of_transport_description varchar(250) NULL,
    dcsa_transport_type varchar(50) NULL UNIQUE
);

CREATE TABLE booking (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_booking_request_reference varchar(100) NOT NULL DEFAULT uuid_generate_v4()::text,
    document_status varchar(4) NOT NULL REFERENCES shipment_event_type(shipment_event_type_code) CHECK(document_status IN ('RECE', 'PENU', 'REJE', 'CONF','PENC', 'CANC', 'DECL', 'CMPL')),
    receipt_type_at_origin varchar(3) NOT NULL REFERENCES receipt_delivery_type(receipt_delivery_type_code),
    delivery_type_at_destination varchar(3) NOT NULL REFERENCES receipt_delivery_type(receipt_delivery_type_code),
    cargo_movement_type_at_origin varchar(3) NOT NULL REFERENCES cargo_movement_type(cargo_movement_type_code),
    cargo_movement_type_at_destination varchar(3) NOT NULL REFERENCES cargo_movement_type(cargo_movement_type_code),
    booking_request_datetime timestamp with time zone NOT NULL,
    service_contract_reference varchar(30) NOT NULL,
    payment_term_code varchar(3) NULL REFERENCES payment_term_type(payment_term_code),
    is_partial_load_allowed boolean NOT NULL,
    is_export_declaration_required boolean NOT NULL,
    export_declaration_reference varchar(35) NULL,
    is_import_license_required boolean NOT NULL,
    import_license_reference varchar(35) NULL,
    is_ams_aci_filing_required boolean NULL,
    is_destination_filing_required boolean NULL,
    contract_quotation_reference varchar(35) NULL,
    incoterms varchar(3) NULL REFERENCES incoterms(incoterms_code),
    invoice_payable_at_id uuid NULL REFERENCES location(id),
    expected_departure_date date NULL,
    expected_arrival_at_place_of_delivery_start_date date NULL CHECK ((expected_arrival_at_place_of_delivery_start_date IS NULL) OR (expected_arrival_at_place_of_delivery_end_date IS NULL) OR expected_arrival_at_place_of_delivery_start_date <= expected_arrival_at_place_of_delivery_end_date),
    expected_arrival_at_place_of_delivery_end_date date NULL,
    transport_document_type_code varchar(3) NULL REFERENCES transport_document_type(transport_document_type_code),
    transport_document_reference varchar(20) NULL,
    booking_channel_reference varchar(20) NULL,
    communication_channel_code varchar(2) NOT NULL REFERENCES communication_channel_qualifier(communication_channel_qualifier_code),
    is_equipment_substitution_allowed boolean NOT NULL,
    vessel_id uuid NULL REFERENCES vessel(id),
    declared_value_currency_code varchar(3) NULL,
    declared_value real NULL,
    place_of_issue_id uuid NULL REFERENCES location(id),
    pre_carriage_mode_of_transport_code varchar(3) NULL REFERENCES mode_of_transport(mode_of_transport_code),
    voyage_id UUID NULL REFERENCES voyage(id)
);

CREATE INDEX ON booking (id);

CREATE TABLE shipment (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    booking_id uuid NOT NULL REFERENCES booking(id),
    carrier_id uuid NOT NULL REFERENCES carrier(id),
    carrier_booking_reference varchar(35) NOT NULL UNIQUE,
    terms_and_conditions text NULL,
    confirmation_datetime timestamp with time zone NOT NULL
);

CREATE TABLE iso_equipment_code (
    iso_equipment_code varchar(4) PRIMARY KEY,
    iso_equipment_name varchar(35) NOT NULL
);


CREATE TABLE reefer_type (
    reefer_type_code varchar(4) PRIMARY KEY,
    reefer_type_name varchar(100) NOT NULL,
    reefer_type_description varchar(250) NOT NULL
);

CREATE TABLE active_reefer_settings (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    reefer_type_code varchar(4) NOT NULL REFERENCES reefer_type (reefer_type_code),
    is_cargo_probe_1_required boolean NOT NULL,
    is_cargo_probe_2_required boolean NOT NULL,
    is_cargo_probe_3_required boolean NOT NULL,
    is_cargo_probe_4_required boolean NOT NULL,
    is_ventilation_open boolean NOT NULL,
    is_drainholes_open boolean NOT NULL,
    is_bulb_mode boolean NOT NULL,
    is_gen_set_required boolean NOT NULL,
    is_pre_cooling_required boolean NOT NULL,
    is_cold_treatment_required boolean NOT NULL,
    is_hot_stuffing_allowed boolean NOT NULL,
    is_tracing_required boolean NOT NULL,
    is_monitoring_required boolean NOT NULL,
    is_high_value_cargo boolean NOT NULL,
    product_name varchar(500) NULL,
    extra_material varchar(500) NULL
);

CREATE TABLE requested_equipment_group (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    booking_id uuid NULL REFERENCES booking (id),
    shipment_id uuid NULL REFERENCES shipment (id),
    requested_equipment_iso_equipment_code varchar(4) NULL REFERENCES iso_equipment_code (iso_equipment_code),
    requested_equipment_units real NULL,
    confirmed_equipment_iso_equipment_code varchar(4) NULL REFERENCES iso_equipment_code (iso_equipment_code),
    confirmed_equipment_units integer NULL,
    is_shipper_owned boolean NOT NULL DEFAULT false,
    active_reefer_settings_id uuid NULL REFERENCES active_reefer_settings (id)
);

CREATE INDEX ON requested_equipment_group (booking_id);

CREATE TABLE commodity (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    booking_id uuid NOT NULL REFERENCES booking(id),
    commodity_type varchar(550) NOT NULL,
    hs_code varchar(10) NULL REFERENCES hs_code (hs_code),
    cargo_gross_weight real NULL,
    cargo_gross_weight_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (cargo_gross_weight_unit IN ('KGM','LBR')),
    cargo_gross_volume real NULL,
    cargo_gross_volume_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (cargo_gross_volume_unit IN ('MTQ','FTQ')),
    number_of_packages integer NULL,
    export_license_issue_date date NULL,
    export_license_expiry_date date NULL
);

CREATE INDEX ON commodity (booking_id);

CREATE TABLE requested_equipment_commodity (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    requested_equipment_id uuid NOT NULL REFERENCES requested_equipment_group (id),
    commodity_id uuid NOT NULL REFERENCES commodity(id)
);

CREATE TABLE shipment_cutoff_time (
    shipment_id uuid NOT NULL REFERENCES shipment(id),
    cut_off_time_code varchar(3) NOT NULL REFERENCES cut_off_time(cut_off_time_code),
    cut_off_time timestamp with time zone NOT NULL,
    PRIMARY KEY (shipment_id, cut_off_time_code)
);


CREATE TABLE displayed_address (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    address_line_1 varchar(35),
    address_line_2 varchar(35),
    address_line_3 varchar(35),
    address_line_4 varchar(35),
    address_line_5 varchar(35)
);

CREATE TABLE shipping_instruction (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    shipping_instruction_reference  varchar(100) NOT NULL DEFAULT uuid_generate_v4()::text,
    document_status varchar(4) NOT NULL REFERENCES shipment_event_type(shipment_event_type_code) CHECK(document_status IN ('RECE','PENU','DRFT','PENA','APPR','ISSU','SURR','VOID')),
    is_shipped_onboard_type boolean NOT NULL,
    number_of_copies_with_charges integer NULL,
    number_of_copies_without_charges integer NULL,
    number_of_originals_with_charges integer NULL,
    number_of_originals_without_charges integer NULL,
    is_electronic boolean NOT NULL,
    is_to_order boolean NOT NULL,
    place_of_issue_id uuid NULL REFERENCES location(id),
    transport_document_type_code varchar(3) NULL REFERENCES transport_document_type(transport_document_type_code),
    displayed_name_for_place_of_receipt uuid NULL REFERENCES displayed_address(id),
    displayed_name_for_port_of_load uuid NULL REFERENCES displayed_address(id),
    displayed_name_for_port_of_discharge uuid NULL REFERENCES displayed_address(id),
    displayed_name_for_place_of_delivery uuid NULL REFERENCES displayed_address(id),
    amendment_to_transport_document_id uuid NULL
);

CREATE TABLE value_added_service_request (
    booking_id uuid NOT NULL REFERENCES booking(id),
    value_added_service_code varchar(5) NOT NULL REFERENCES value_added_service_code (value_added_service_code)
);

CREATE INDEX ON value_added_service_request (booking_id);

CREATE TABLE transport_document (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    transport_document_reference varchar(20) NOT NULL DEFAULT LEFT(uuid_generate_v4()::text, 20),
    place_of_issue_id uuid NULL REFERENCES location(id),
    issue_date date NULL,
    shipped_onboard_date date NULL,
    received_for_shipment_date date NULL,
    carrier_id uuid NOT NULL REFERENCES carrier(id),
    shipping_instruction_id uuid NOT NULL REFERENCES shipping_instruction (id),
    number_of_rider_pages integer NULL,
    issuing_party_id uuid NOT NULL REFERENCES party(id)
);

ALTER TABLE shipping_instruction
    ADD FOREIGN KEY (amendment_to_transport_document_id) REFERENCES transport_document (id);

CREATE TABLE carrier_clauses (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    clause_content text NOT NULL
);

CREATE TABLE shipment_carrier_clauses (
    carrier_clause_id uuid NOT NULL REFERENCES carrier_clauses (id),
    shipment_id uuid NULL REFERENCES shipment (id),
    transport_document_id uuid NULL REFERENCES transport_document (id)
);

CREATE TABLE party_function (
    party_function_code varchar(3) PRIMARY KEY,
    party_function_name varchar(100) NOT NULL,
    party_function_description varchar(350) NOT NULL
);


CREATE TABLE document_party (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    party_id uuid NOT NULL REFERENCES party (id),
    shipping_instruction_id uuid NULL REFERENCES shipping_instruction (id),
    shipment_id uuid NULL REFERENCES shipment (id),
    party_function varchar(3) NOT NULL REFERENCES party_function (party_function_code),
    is_to_be_notified boolean NOT NULL,
    booking_id uuid NULL REFERENCES booking(id),
    displayed_address_id uuid NULL REFERENCES displayed_address(id)
);

-- Supporting FK constraints
CREATE INDEX ON document_party (party_id);
CREATE INDEX ON document_party (party_function);
CREATE INDEX ON document_party (shipment_id);
CREATE INDEX ON document_party (shipping_instruction_id);
CREATE INDEX ON document_party (booking_id);

CREATE TABLE charge (
    id varchar(100) PRIMARY KEY,
    transport_document_id uuid NOT NULL REFERENCES transport_document(id),
    shipment_id uuid NULL REFERENCES shipment (id),
    charge_type varchar(20) NOT NULL,
    currency_amount real NOT NULL,
    currency_code varchar(3) NOT NULL,
    payment_term_code varchar(3) NOT NULL REFERENCES payment_term_type(payment_term_code),
    calculation_basis varchar(50) NOT NULL,
    unit_price real NOT NULL,
    quantity real NOT NULL
);

CREATE TABLE document_version (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    transport_document_id uuid NOT NULL REFERENCES transport_document (id),
    document_status varchar(4) NOT NULL REFERENCES shipment_event_type (shipment_event_type_code),
    binary_copy bytea NOT NULL,
    document_hash text NOT NULL,
    last_modified_datetime timestamp with time zone NOT NULL
);

CREATE TABLE equipment (
    equipment_reference varchar(15) PRIMARY KEY,    -- The unique identifier for the equipment, which should follow the BIC ISO Container Identification Number where possible. According to ISO 6346, a container identification code consists of a 4-letter prefix and a 7-digit number (composed of a 3-letter owner code, a category identifier, a serial number and a check-digit). If a container does not comply with ISO 6346, it is suggested to follow Recommendation #2 “Container with non-ISO identification” from SMDG.
    -- Unique code for the different equipment size/type used for transporting commodities. The code is a concatenation of ISO Equipment Size Code and ISO Equipment Type Code A and follows the ISO 6346 standard.
    iso_equipment_code char(4) NOT NULL REFERENCES iso_equipment_code (iso_equipment_code),
    tare_weight real NOT NULL,
    total_max_weight real null,
    weight_unit varchar(3) NOT NULL REFERENCES unit_of_measure(unit_of_measure_code)  CHECK (weight_unit IN ('KGM','LBR'))
);

-- Supporting FK constraints
CREATE INDEX ON equipment (iso_equipment_code);
CREATE INDEX ON equipment (equipment_reference);


CREATE TABLE package_code (
    package_code varchar(3) PRIMARY KEY,
    package_code_description varchar(50) NOT NULL
);

CREATE TABLE utilized_transport_equipment (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    equipment_reference varchar(15) NOT NULL REFERENCES equipment (equipment_reference),
    cargo_gross_weight real NULL,
    cargo_gross_weight_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (cargo_gross_weight_unit IN ('KGM','LBR')),
    is_shipper_owned boolean NOT NULL,
    requested_equipment_group_id uuid NULL REFERENCES requested_equipment_group (id)
);

-- Supporting FK constraints
CREATE INDEX ON utilized_transport_equipment (equipment_reference);

CREATE TABLE setpoint (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    active_reefer_settings_id uuid NOT NULL REFERENCES active_reefer_settings (id),
    temperature real NULL,
    temperature_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (temperature_unit IN ('CEL','FAH')),
    humidity real NULL,
    air_exchange real NULL,
    air_exchange_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (air_exchange_unit IN ('MQH','FQH')),
    o2 real NULL,
    co2 real NULL,
    days_prior_to_discharge real NULL
);

CREATE TABLE consignment_item (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    description_of_goods text NOT NULL,
    shipping_instruction_id uuid NOT NULL REFERENCES shipping_instruction (id),
    shipment_id uuid NOT NULL REFERENCES shipment (id),
    commodity_id uuid NULL REFERENCES commodity (id)
);

-- Supporting FK constraints
CREATE INDEX ON consignment_item (shipping_instruction_id);
CREATE INDEX ON consignment_item (shipment_id);
CREATE INDEX ON consignment_item (commodity_id);

CREATE TABLE cargo_item (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    consignment_item_id uuid NOT NULL REFERENCES consignment_item(id),
    weight real NOT NULL,
    volume real NULL,
    weight_unit varchar(3) NOT NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (weight_unit IN ('KGM','LBR')),
    volume_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (volume_unit IN ('MTQ','FTQ')),
    number_of_packages integer NOT NULL,
    package_code varchar(3) NOT NULL REFERENCES package_code (package_code),
    utilized_transport_equipment_id uuid NOT NULL REFERENCES utilized_transport_equipment (id),
    package_name_on_bl varchar(50) NULL
);

-- Supporting FK constraints
CREATE INDEX ON cargo_item (consignment_item_id);
CREATE INDEX ON cargo_item (package_code);
CREATE INDEX ON cargo_item (utilized_transport_equipment_id);

CREATE TABLE cargo_line_item (
    cargo_line_item_id text NOT NULL,
    cargo_item_id uuid NOT NULL REFERENCES cargo_item (id),
    shipping_marks text NOT NULL,
    -- Choice of cargo_item_id as first member is deliberate as it enables the
    -- underlying index to be used for FK checks as well (without a separate index
    -- because Postgres currently always creates an INDEX for UNIQUE constraints)
    UNIQUE (cargo_item_id, cargo_line_item_id)
);

CREATE TABLE reference (
    reference_type_code varchar(3) NOT NULL REFERENCES reference_type (reference_type_code),
    reference_value varchar(100) NOT NULL,
    shipment_id uuid NULL REFERENCES shipment (id),
    shipping_instruction_id uuid NULL REFERENCES shipping_instruction (id),
    booking_id uuid NULL REFERENCES booking(id),
    consignment_item_id uuid NULL REFERENCES consignment_item(id)
);

CREATE INDEX ON reference (booking_id);
CREATE INDEX ON reference (consignment_item_id);

CREATE TABLE seal_source (
    seal_source_code varchar(5) PRIMARY KEY,
    seal_source_description varchar(50) NOT NULL
);

CREATE TABLE seal_type (
    seal_type_code varchar(5) PRIMARY KEY,
    seal_type_description varchar(50) NOT NULL
);

CREATE TABLE seal (
    utilized_transport_equipment_id uuid NOT NULL REFERENCES utilized_transport_equipment (id),
    seal_number varchar(15) NOT NULL,
    seal_source_code varchar(5) NULL REFERENCES seal_source (seal_source_code),
    seal_type_code varchar(5) REFERENCES seal_type (seal_type_code)
);
-- Supporting FK constraints
CREATE INDEX ON seal (utilized_transport_equipment_id);
CREATE INDEX ON seal (seal_source_code);
CREATE INDEX ON seal (seal_type_code);

CREATE TABLE shipment_location_type (
    shipment_location_type_code varchar(3) PRIMARY KEY,
    shipment_location_type_name varchar(50) NOT NULL,
    shipment_location_type_description varchar(250) NOT NULL
);

-- Supporting FK constraints
CREATE INDEX ON un_location (country_code);

-- Supporting FK constraints
CREATE INDEX ON location (un_location_code);

CREATE TABLE shipment_location (
    shipment_id uuid NULL REFERENCES shipment (id),
    booking_id uuid NULL REFERENCES booking(id),
    location_id uuid NOT NULL REFERENCES location (id),
    shipment_location_type_code varchar(3) NOT NULL REFERENCES shipment_location_type (shipment_location_type_code),
    event_date_time timestamp with time zone NULL, --optional datetime indicating when the event at the location takes place
    UNIQUE (location_id, shipment_location_type_code, shipment_id)
);

-- Supporting FK constraints
-- Note the omission of INDEX for "location_id" is deliberate; we rely on the implicit INDEX from the
-- UNIQUE constraint for that.
CREATE INDEX ON shipment_location (shipment_location_type_code);
CREATE INDEX ON shipment_location (shipment_id);
CREATE INDEX ON shipment_location (booking_id);

CREATE TABLE port_call_status_type (
    port_call_status_type_code varchar(4) NOT NULL PRIMARY KEY,
    port_call_status_type_name varchar(30) NOT NULL,
    port_call_status_type_description varchar(250) NOT NULL
);

CREATE TABLE transport_call (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    transport_call_reference varchar(100) NOT NULL DEFAULT uuid_generate_v4(),
    transport_call_sequence_number integer,
    facility_type_code char(4) NULL REFERENCES facility_type (facility_type_code),
    location_id uuid NULL REFERENCES location (id),
    mode_of_transport_code varchar(3) NULL REFERENCES mode_of_transport (mode_of_transport_code),
    vessel_id uuid NULL REFERENCES vessel(id),
    import_voyage_id uuid NULL, -- references on line 800
    export_voyage_id uuid NULL, -- references on line 800
    port_call_status_type_code char(4) NULL REFERENCES port_call_status_type (port_call_status_type_code),
    port_visit_reference varchar(50) NULL
);

CREATE TABLE transport (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    transport_reference varchar(50) NULL,
    transport_name varchar(100) NULL,
    load_transport_call_id uuid NOT NULL REFERENCES transport_call(id),
    discharge_transport_call_id uuid NOT NULL REFERENCES transport_call(id)
);

CREATE TABLE commercial_voyage (
    commercial_voyage_id uuid PRIMARY KEY,
    commercial_voyage_name text NOT NULL
);

CREATE TABLE transport_plan_stage_type (
    transport_plan_stage_code varchar(3) PRIMARY KEY,
    transport_plan_stage_name varchar(100) NOT NULL,
    transport_plan_stage_description varchar(250) NOT NULL
);

CREATE TABLE shipment_transport (
    shipment_id uuid NULL REFERENCES shipment(id),
    transport_id uuid NOT NULL REFERENCES transport(id),
    transport_plan_stage_sequence_number integer NOT NULL,
    transport_plan_stage_code varchar(3) NOT NULL REFERENCES transport_plan_stage_type(transport_plan_stage_code),
    commercial_voyage_id uuid NULL REFERENCES commercial_voyage(commercial_voyage_id),
    is_under_shippers_responsibility boolean NOT NULL,
    UNIQUE (shipment_id, transport_id, transport_plan_stage_sequence_number) -- transport_plan_stage_sequence_number must be unique together with transport and shipment
);

CREATE TABLE event_classifier (
    event_classifier_code varchar(3) PRIMARY KEY,
    event_classifier_name varchar(30) NOT NULL,
    event_classifier_description varchar(250) NOT NULL
);

CREATE TABLE equipment_event_type (
    equipment_event_type_code varchar(4) PRIMARY KEY,
    equipment_event_type_name varchar(35) NOT NULL,
    equipment_event_type_description varchar(300) NOT NULL
);

CREATE TABLE document_type (
    document_type_code varchar(3) PRIMARY KEY,
    document_type_name varchar(100) NOT NULL,
    document_type_description varchar(250) NOT NULL
);

CREATE TABLE transport_event_type (
    transport_event_type_code varchar(4) PRIMARY KEY,
    transport_event_type_name varchar(30) NOT NULL,
    transport_event_type_description varchar(250) NOT NULL
);

CREATE TABLE empty_indicator (
    empty_indicator_code varchar(5) PRIMARY KEY
);

CREATE TABLE event (
    event_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    event_classifier_code varchar(3) NOT NULL REFERENCES event_classifier(event_classifier_code),
    event_created_date_time timestamp with time zone DEFAULT now() NOT NULL,
    event_date_time timestamp with time zone NOT NULL
);

CREATE TABLE equipment_event (
    equipment_event_type_code varchar(4) NOT NULL REFERENCES equipment_event_type(equipment_event_type_code),
    equipment_reference varchar(15) NULL REFERENCES equipment (equipment_reference),
    empty_indicator_code varchar(5) NULL REFERENCES empty_indicator(empty_indicator_code),
    transport_call_id uuid NULL REFERENCES transport_call(id),
    facility_type_code char(4) NULL REFERENCES facility_type (facility_type_code) CONSTRAINT facility_type_code CHECK(facility_type_code IN ('BOCR','CLOC','COFS','OFFD','DEPO','INTE','POTE','RAMP')),
    is_transshipment_move boolean NOT NULL default false,
    event_location_id uuid NULL REFERENCES location(id)
) INHERITS (event);

ALTER TABLE equipment_event ADD PRIMARY KEY (event_id);

CREATE TABLE shipment_event (
    shipment_event_type_code varchar(4) NOT NULL REFERENCES shipment_event_type(shipment_event_type_code),
    document_type_code varchar(3) NOT NULL REFERENCES document_type(document_type_code),
    document_id uuid NOT NULL,
    reason varchar(250) NULL
) INHERITS (event);

ALTER TABLE shipment_event ADD PRIMARY KEY (event_id),
                                        ADD CONSTRAINT event_classifier_code_is_act CHECK (event_classifier_code = 'ACT');

CREATE TABLE smdg_delay_reason (
    delay_reason_code varchar(3) NOT NULL PRIMARY KEY,
    delay_reason_name varchar(100) NOT NULL,
    delay_reason_description varchar(250) NOT NULL
);

CREATE TABLE transport_event (
    transport_event_type_code varchar(4) NOT NULL REFERENCES transport_event_type(transport_event_type_code),
    delay_reason_code varchar(3) NULL REFERENCES smdg_delay_reason(delay_reason_code),
    change_remark varchar(250),
    transport_call_id uuid NULL REFERENCES transport_call(id)
) INHERITS (event);

ALTER TABLE transport_event ADD PRIMARY KEY (event_id);

CREATE TABLE vessel_sharing_agreement_partner (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_id uuid NOT NULL REFERENCES carrier(id),
    vessel_sharing_agreement_id uuid NOT NULL REFERENCES vessel_sharing_agreement(id)
);

CREATE TABLE service_proforma (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    service_proforma_agreed_date_time timestamp with time zone NOT NULL,
    port_call_sequence_number integer NULL,
    port_code varchar(5) NULL,
    port_terminal_call_sequence_number integer NULL,
    port_terminal_code varchar(11) NULL,
    service_id uuid NULL REFERENCES service (id)
);

ALTER TABLE transport_call
    ADD FOREIGN KEY (import_voyage_id) REFERENCES voyage (id) INITIALLY DEFERRED;
ALTER TABLE transport_call
    ADD FOREIGN KEY (export_voyage_id) REFERENCES voyage (id) INITIALLY DEFERRED;

CREATE TABLE commercial_voyage_transport_call (
    transport_call_id uuid NOT NULL REFERENCES transport_call(id),
    commercial_voyage_id uuid NOT NULL REFERENCES commercial_voyage(commercial_voyage_id)
);

CREATE TABLE operations_event_type (
    operations_event_type_code varchar(4) NOT NULL PRIMARY KEY,
    operations_event_type_name varchar(30) NOT NULL,
    operations_event_type_description varchar(250) NOT NULL
);

CREATE TABLE port_call_service_type (
    port_call_service_type_code varchar(4) NOT NULL PRIMARY KEY,
    port_call_service_type_name varchar(30) NOT NULL,
    port_call_service_type_description varchar(250) NOT NULL
);

CREATE TABLE port_call_phase_type (
     port_call_phase_type_code varchar(4) NOT NULL PRIMARY KEY,
     port_call_phase_type_name varchar(30) NOT NULL,
     port_call_phase_type_description varchar(250) NOT NULL
);

CREATE TABLE operations_event (
    publisher_id uuid NOT NULL REFERENCES party(id),
    publisher_role varchar(3) NOT NULL REFERENCES party_function(party_function_code) CHECK(publisher_role IN ('CA', 'AG', 'VSL', 'ATH', 'PLT', 'TR', 'TWG', 'BUK', 'LSH', 'SLU', 'SVP', 'MOR')),
    operations_event_type_code varchar(4) NOT NULL REFERENCES operations_event_type(operations_event_type_code),
    event_location_id uuid NULL REFERENCES location (id),
    transport_call_id uuid NOT NULL REFERENCES transport_call(id),
    port_call_service_type_code varchar(4) NULL REFERENCES port_call_service_type(port_call_service_type_code),
    facility_type_code varchar(4) NULL REFERENCES facility_type(facility_type_code) CHECK(facility_type_code IN ('PBPL', 'BRTH','ANCH')),
    delay_reason_code varchar(3) NULL REFERENCES smdg_delay_reason(delay_reason_code),
    vessel_position_id uuid NULL REFERENCES location (id),
    remark varchar(500) NULL,
    port_call_phase_type_code varchar(4) NULL REFERENCES port_call_phase_type(port_call_phase_type_code),
    vessel_draft real NULL,
    vessel_draft_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CONSTRAINT vessel_draft_unit CHECK (vessel_draft_unit IN ('FOT','MTR')),
    miles_to_destination_port real NULL
) INHERITS (event);

ALTER TABLE operations_event ADD PRIMARY KEY (event_id);

