\connect dcsa_openapi

--- Insert data into v2_0 model ---

INSERT INTO dcsa_v2_0.schedule (
    id,
    vessel_operator_carrier_code,
    vessel_partner_carrier_code,
    start_date,
    date_range,
    vessel_operator_carrier_code_list_provider,
    vessel_partner_carrier_code_list_provider
) VALUES (
    uuid('35b7b170-c751-11ea-a305-7b347bb9119f'),
    'ZIM',
    'MSK',
    DATE '2020-07-16',
    INTERVAL '3 weeks',
    'SMDG',
    'SMDG'
);

INSERT INTO dcsa_v2_0.schedule (
    id,
    vessel_operator_carrier_code,
    vessel_partner_carrier_code,
    start_date,
    date_range,
    vessel_operator_carrier_code_list_provider,
    vessel_partner_carrier_code_list_provider
) VALUES (
    uuid('35b7b170-c751-11ea-a305-7b347bb91100'),
    'HPL',
    'ONE',
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
) VALUES (
    uuid('8b64d20b-523b-4491-b2e5-32cfa5174eee'),
    uuid('35b7b170-c751-11ea-a305-7b347bb9119f'),
    'Y6S',
    9466960,
    'NORTHERN JASPER',
    '2007W',
    'ITGOA',
    'Genoa',
    3,
    'TERM',
    'ITGOAASEA',
    NULL
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
) VALUES (
    uuid('8b64d20b-523b-4491-b2e5-32cfa5174eed'),
    uuid('35b7b170-c751-11ea-a305-7b347bb9119f'),
    'Y6S',
    9466960,
    'NORTHERN JASPER',
    '2007W',
    'ITGOA',
    'Genoa',
    3,
    'TERM',
    'ITGOAASEA',
    NULL
);

INSERT INTO dcsa_v2_0.shipment (
    id,
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
    uuid('6e2d856c-d871-11ea-a630-03e5334d1811'),
    'BOL',
    'Magic Wands Inc.',
    'The Mage Guild',
    '5, Mountain Road, The land beyond the sea',
    DATE '2020-03-10',
    '125, Valley Street, The land nearby',
    DATE '2020-04-10',
    'SMDG-ZIM'
);

INSERT INTO dcsa_v2_0.shipment (
    id,
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
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea1'),
    'BR1239719871',
    DATE '2020-03-07',
    uuid('ae2d856c-d871-11ea-a630-03e5334d1811'),
    'BOL',
    'Magic Wands Inc.',
    'The Mage Guild',
    '5, Mountain Road, The land beyond the sea',
    DATE '2020-03-10',
    '125, Valley Street, The land nearby',
    DATE '2020-04-10',
    'SMDG-ZIM'
);

INSERT INTO dcsa_v2_0.shipment (
    id,
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
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea2'),
    'BR1239719872',
    DATE '2021-03-07',
    uuid('be2d856c-d871-11ea-a630-03e5334d1811'),
    'BOL',
    'Unicorn Inc.',
    'Long horn',
    '5, Lake View, By the lake',
    DATE '2021-05-23',
    '125, Wall St, Next door',
    DATE '2020-08-01',
    'SMDG-ZIM'
);

INSERT INTO dcsa_v2_0.shipment_event (
    event_id,
    event_classifier_code,
    event_type,
    event_date_time,
    event_type_code,
    shipment_id,
    shipment_information_type_code
) VALUES (
    uuid('784871e7-c9cd-4f59-8d88-2e033fa799a1'),
    'PLN',
    'SHIPMENT',
    '2020-07-15',
    'DEPA',
    uuid('6e2d856c-d871-11ea-a630-03e5334d1800'),
    'WTF'
);

INSERT INTO dcsa_v2_0.shipment_event (
    event_id,
    event_classifier_code,
    event_type,
    event_date_time,
    event_type_code,
    shipment_id,
    shipment_information_type_code
) VALUES (
    uuid('e48f2bc0-c746-11ea-a3ff-db48243a89f4'),
    'PLN',
    'SHIPMENT',
    TO_DATE('2020/07/15 13:14:15', 'yyyy/mm/dd hh24:mi:ss'),
    'DEPA',
    uuid('6e2d856c-d871-11ea-a630-03e5334d1800'),
    'WTF'
);

INSERT INTO dcsa_v2_0.shipment_event (
    event_id,
    event_classifier_code,
    event_type,
    event_date_time,
    event_type_code,
    shipment_id,
    shipment_information_type_code
) VALUES (
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea1'),
    'PLN',
    'SHIPMENT',
    TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),
    'ARRI',
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea1'),
    'WTF'
);

INSERT INTO dcsa_v2_0.equipment_event (
    event_id,
    event_classifier_code,
    event_type,
    event_date_time,
    event_type_code,
    transport_call_id,
    equipment_reference,
    empty_indicator_code
) VALUES (
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea2'),
    'ACT',
    'EQUIPMENT',
    TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),
    'ARRI',
    uuid('8b64d20b-523b-4491-b2e5-32cfa5174eed'),
    'equipref3453',
    'EMPTY'
);

INSERT INTO dcsa_v2_0.transport_event (
    event_id,
    event_classifier_code,
    event_type,
    event_date_time,
    event_type_code,
    transport_call_id,
    delay_reason_code,
    vessel_schedule_change_remark
) VALUES (
    uuid('5e51e72c-d872-11ea-811c-0f8f10a32ea3'),
    'ACT',
    'TRANSPORT',
    TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),
    'DEPA',
    uuid('8b64d20b-523b-4491-b2e5-32cfa5174eed'),
    'ABC',
    'Do not know a valid delay reason code...'
);

INSERT INTO dcsa_v2_0.event_subscription (
    callback_url,
    event_type,
    booking_reference,
    transport_document_id,
    transport_document_type,
    equipment_reference
) VALUES (
    'http://localhost:4567/webhook/receive-transport-events',
    'TRANSPORT',
    '',
    '',
    '',
    ''
);

INSERT INTO dcsa_v2_0.event_subscription (
    callback_url,
    event_type,
    booking_reference,
    transport_document_id,
    transport_document_type,
    equipment_reference
) VALUES (
    'http://172.17.0.1:4567/webhook/receive-transport-events',
    'TRANSPORT',
    '',
    '',
    '',
    ''
);

INSERT INTO dcsa_v2_0.event_subscription (
    callback_url,
    event_type,
    booking_reference,
    transport_document_id,
    transport_document_type,
    equipment_reference
) VALUES (
    'http://172.17.0.1:4567/webhook/receive',
    '',
    '',
    '',
    '',
    ''
);

INSERT INTO dcsa_v2_0.event_subscription (
    callback_url,
    event_type,
    booking_reference,
    transport_document_id,
    transport_document_type,
    equipment_reference
) VALUES (
    'http://localhost:4567/webhook/receive',
    '',
    '',
    '',
    '',
    ''
);
