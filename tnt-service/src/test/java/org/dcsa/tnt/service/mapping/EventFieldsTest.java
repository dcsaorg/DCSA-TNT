package org.dcsa.tnt.service.mapping;

import org.dcsa.skernel.domain.persistence.entity.Address;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.domain.persistence.entity.Facility;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.test.helpers.FieldValidator;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.ShipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.persistence.entity.Reference;
import org.dcsa.tnt.persistence.entity.Seal;
import org.dcsa.tnt.persistence.entity.Service;
import org.dcsa.tnt.persistence.entity.TransportCall;
import org.dcsa.tnt.persistence.entity.Vessel;
import org.dcsa.tnt.persistence.entity.Voyage;
import org.junit.jupiter.api.Test;

public class EventFieldsTest {
  @Test
  public void testShipmentEventFields() {
    FieldValidator.assertFieldsAreEqual(ShipmentEvent.class, org.dcsa.tnt.service.domain.ShipmentEvent.class,
      // Filled out separately
      "eventType", "references", "relatedDocumentReferences");
  }

  @Test
  public void testTransportEventFields() {
    FieldValidator.assertFieldsAreEqual(TransportEvent.class, org.dcsa.tnt.service.domain.TransportEvent.class,
      // Filled out separately
      "eventType", "references", "relatedDocumentReferences");
  }

  @Test
  public void testEquipmentEventFields() {
    FieldValidator.assertFieldsAreEqual(EquipmentEvent.class, org.dcsa.tnt.service.domain.EquipmentEvent.class,
      // Filled out separately
      "eventType", "references", "relatedDocumentReferences", "seals");
  }

  @Test
  public void testAddressFields() {
    FieldValidator.assertFieldsAreEqual(Address.class, org.dcsa.tnt.service.domain.Address.class);
  }

  @Test
  public void testCarrierFields() {
    FieldValidator.assertFieldsAreEqual(Carrier.class, org.dcsa.tnt.service.domain.Carrier.class);
  }

  @Test
  public void testFacilityFields() {
    FieldValidator.assertFieldsAreEqual(Facility.class, org.dcsa.tnt.service.domain.Facility.class);
  }

  @Test
  public void testLocationFields() {
    FieldValidator.assertFieldsAreEqual(Location.class, org.dcsa.tnt.service.domain.Location.class);
  }

  @Test
  public void testReferenceFields() {
    FieldValidator.assertFieldsAreEqual(Reference.class, org.dcsa.tnt.service.domain.Reference.class,
      // Not mapped
      "bookingID", "utilizedEquipmentID","transportCallID", "documentID","linkType"
    );
  }

  @Test
  public void testSealFields() {
    FieldValidator.assertFieldsAreEqual(Seal.class, org.dcsa.tnt.service.domain.Seal.class);
  }

  @Test
  public void testServiceFields() {
    FieldValidator.assertFieldsAreEqual(Service.class, org.dcsa.tnt.service.domain.Service.class);
  }

  @Test
  public void testTransportCallFields() {
    FieldValidator.assertFieldsAreEqual(TransportCall.class, org.dcsa.tnt.service.domain.TransportCall.class);
  }

  @Test
  public void testVesselFields() {
    FieldValidator.assertFieldsAreEqual(Vessel.class, org.dcsa.tnt.service.domain.Vessel.class);
  }

  @Test
  public void testVoyageFields() {
    FieldValidator.assertFieldsAreEqual(Voyage.class, org.dcsa.tnt.service.domain.Voyage.class);
  }
}
