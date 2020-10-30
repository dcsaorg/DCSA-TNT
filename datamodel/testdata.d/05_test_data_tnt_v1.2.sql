\connect dcsa_openapi

--- Insert data into v1_2 model ---

INSERT INTO dcsa_tnt_v1_2.shipment_event (
    event_classifier_code,
    event_type,
    event_date_time,
    event_id,
    event_type_code,
    shipment_id,
    shipment_information_type_code
) VALUES (
    'PLN',
    'SHIPMENT',
    TO_DATE('2020/07/15 13:14:15', 'yyyy/mm/dd hh24:mi:ss'),
    'e48f2bc0-c746-11ea-a3ff-db48243a89f4',
    'DEPA',
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea1'),
    'Some type code text'
);

INSERT INTO "dcsa_tnt_v1_2".shipment_event (
    event_classifier_code,
    event_type,
    event_date_time,
    event_type_code,
    shipment_id,
    shipment_information_type_code
) VALUES (
    'PLN',
    'SHIPMENT',
    TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),
    'ARRI',
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea1'),
    'shipment_type_code'
);

INSERT INTO "dcsa_tnt_v1_2".equipment_event (
	event_classifier_code,
	event_type,
	event_date_time,
	event_type_code,
	equipment_reference,
	facility_type_code,
	un_location_code,
	facility_code,
	other_facility,
	empty_indicator_code
) VALUES (
    'ACT', 'EQUIPMENT',
    TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),
    'ARRI',
    'equipref3453',
    'HYDRO',
    'LA',
    'LAHO',
    '',
    'EMPTY'
);

INSERT INTO "dcsa_tnt_v1_2".transport_equipment_event (
	event_classifier_code,
	event_type,
	event_date_time,
	event_type_code,
	equipment_reference,
	facility_type_code,
	un_location_code,
	facility_code,
	other_facility,
	empty_indicator_code,
	transport_reference,
	transport_leg_reference,
	mode_of_transport_code
) VALUES (
    'PLN', 'TRANSPORTEQUIPMENT',
    TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),
    'ARRI',
    'eqref123',
    'factory',
    'CPH',
    '2',
    'no',
    'LADEN',
    'ref123',
    'legref',
    '7'
);

INSERT INTO "dcsa_tnt_v1_2".transport_event (
	event_classifier_code,
	event_type,
	event_date_time,
	event_type_code,
	transport_reference,
	transport_leg_reference,
	facility_type_code,
	un_location_code,
	facility_code,
	other_facility,
	mode_of_transport_code
) VALUES (
    'ACT', 'TRANSPORT',
    TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),
    'DEPA',
    'transportref123',
    'legreference234',
    'coal',
    'PORT',
    'NYC',
    'no',
    '2'
);

INSERT INTO dcsa_tnt_v1_2.event_subscription (
    callback_url,
    event_type,
    booking_reference,
    bill_of_lading_number,
    equipment_reference
) VALUES (
    'http://localhost:4567/webhook/receive',
    '',
    '',
 '',
    ''
);

INSERT INTO dcsa_tnt_v1_2.event_subscription (
    callback_url,
    event_type,
    booking_reference,
    bill_of_lading_number,
    equipment_reference
) VALUES (
    'http://localhost:4567/webhook/receive-transport-events',
    'TRANSPORT',
    '',
    '',
    ''
);

INSERT INTO dcsa_tnt_v1_2.event_subscription (
    callback_url,
    event_type,
    booking_reference,
    bill_of_lading_number,
    equipment_reference
) VALUES (
    'http://172.17.0.1:4567/webhook/receive',
    '',
    '',
    '',
    ''
);

INSERT INTO dcsa_tnt_v1_2.shipment (
    id,
    booking_reference,
    booking_datetime,
    bill_of_lading_number,
    shipper_name,
    consignee_name,
    collection_origin,
    collection_dateTime,
    delivery_destination,
    delivery_datetime,
    carrier_code
) VALUES (
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea1'),
    'BR1239719871',
    DATE '2020-03-07',
    'BL32147109',
    'Magic Wands Inc.',
    'The Mage Guild',
    '5, Mountain Road, The land beyond the sea',
    DATE '2020-03-10',
    '125, Valley Street, The land nearby',
    DATE '2020-04-10',
    'SMDG-ZIM'
);

INSERT INTO dcsa_tnt_v1_2.shipment (
    id,
    booking_reference,
    booking_datetime,
    bill_of_lading_number,
    shipper_name,
    consignee_name,
    collection_origin,
    collection_dateTime,
    delivery_destination,
    delivery_datetime,
    carrier_code
) VALUES (
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea2'),
    'BR1239719872',
    DATE '2021-03-07',
    'AA32147122',
    'Unicorn Inc.',
    'Long horn',
    '5, Lake View, By the lake',
    DATE '2021-05-23',
    '125, Wall St, Next door',
    DATE '2020-08-01',
    'SMDG-ZIM'
);
