\connect dcsa_openapi

INSERT INTO dcsa_v1_1.shipment_event (
    event_classifier_code,
    event_type,
    event_date_time,
    event_id,
    event_type_code,
    shipment_id,
    shipment_information_type_code
    )
    VALUES ('PLN', 'SHIPMENT', TO_DATE('2020/07/15 13:14:15', 'yyyy/mm/dd hh24:mi:ss'), 'e48f2bc0-c746-11ea-a3ff-db48243a89f4', 'DEPA', uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea1'), 'Some type code text');

INSERT INTO "dcsa_v1_1".equipment_event(
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
	)
	VALUES ('ACT', 'EQUIPMENT', TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'), 'ARRI', 'equipref3453', 'HYDRO', 'LA', 'LAHO', '', 'EMPTY');

INSERT INTO "dcsa_v1_1".transport_equipment_event(
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
	)
	VALUES ('PLN', 'TRANSPORTEQUIPMENT', TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'), 'ARRI', 'eqref123', 'factory', 'CPH', '2', 'no', 'LADEN', 'ref123', 'legref', '7');

INSERT INTO "dcsa_v1_1".transport_event(
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
	)
	VALUES ('ACT', 'TRANSPORT', TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),  'DEPA', 'transportref123', 'legreference234', 'coal', 'PORT', 'NYC', 'no', '2');

INSERT INTO "dcsa_v1_1".shipment_event(
    event_classifier_code,
    event_type,
    event_date_time,
    event_type_code,
    shipment_id,
    shipment_information_type_code
    )
    VALUES ('PLN', 'SHIPMENT', TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'), 'ARRI', uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea1'), 'shipment_type_code');	

INSERT INTO dcsa_v2_0.schedule (
    id,
    vessel_operator_carrier_code,
    vessel_partner_carrier_code,
    start_date,
    date_range,
    vessel_operator_carrier_code_list_provider,
    vessel_partner_carrier_code_list_provider
)
VALUES (
    uuid('35b7b170-c751-11ea-a305-7b347bb9119f'),
    'ZIM',
    'MSK',
    DATE '2020-07-16',
    INTERVAL '3 weeks',
    'SMDG',
    'SMDG'
);

INSERT INTO dcsa_v2_0.transport_call (
    id,
    schedule_id,
    carrier_service_code,
    vessel_imo_number,
    vessel_name,
    carrier_voyage_number,
    un_location_code,
    un_location_name,
    transport_call_number,
    facility_type_code,
    facility_code,
    other_facility
    )
VALUES (
    uuid('8b64d20b-523b-4491-b2e5-32cfa5174eee'),
    uuid('35b7b170-c751-11ea-a305-7b347bb9119f'),
    'Y6S',
    '9466960',
    'NORTHERN JASPER',
    '2007W',
    'ITGOA',
    'Genoa',
    3,
    'TERM',
    'ITGOAASEA',
    NULL);

INSERT INTO dcsa_v2_0.shipment_event (
    id,
    shipment_id,
    event_classifier_code,
    event_type,
    event_date_time,
    event_type_code,
    shipment_information_type_code
    )
VALUES (
    uuid('784871e7-c9cd-4f59-8d88-2e033fa799a1'),
    uuid('6e2d856c-d871-11ea-a630-03e5334d1800'),
    'PLN',
    'SHIPMENT',
    '2020-07-15',
    'DEPA',
    'WTF');


INSERT INTO dcsa_v1_1.event_subscription(
    callback_url,
    event_type,
    booking_reference,
    bill_of_lading_number,
    equipment_reference
)
    VALUES ('http://localhost:4567/webhook/receive', '', '', '', '');

    INSERT INTO dcsa_v1_1.event_subscription(
    callback_url,
    event_type,
    booking_reference,
    bill_of_lading_number,
    equipment_reference
)
    VALUES ('http://localhost:4567/webhook/receive-transport-events', 'TRANSPORT', '', '', '');

INSERT INTO dcsa_v1_1.event_subscription(
    callback_url,
    event_type,
    booking_reference,
    bill_of_lading_number,
    equipment_reference
)
    VALUES ('http://172.17.0.1:4567/webhook/receive', '', '', '', '');


INSERT INTO dcsa_v2_0.shipment(
    shipment_id,
    booking_reference,
    booking_datetime,
    transport_document_id,
    transport_document_type_code,
    shipper_name,
    consignee_name,
    collection_origin,
    collection_dateTime,
    delivery_destination,
    delivery_datetime,
    carrier_code
) VALUES (
    uuid('6e2d856c-d871-11ea-a630-03e5334d1800'),
    'BR1239719871',
    DATE '2020-03-07',
    'BL32147109',
    'BOL',
    'Magic Wands Inc.',
    'The Mage Guild',
    '5, Mountain Road, The land beyond the sea',
    DATE '2020-03-10',
    '125, Valley Street, The land nearby',
    DATE '2020-04-10',
    'SMDG-ZIM'
);

INSERT INTO dcsa_v1_1.shipment(
    shipment_id,
    booking_reference,
    booking_datetime,
    blnumber,
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

INSERT INTO dcsa_v1_1.shipment(
    shipment_id,
    booking_reference,
    booking_datetime,
    blnumber,
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
