package org.dcsa.tnt.service.mapping;

import org.dcsa.skernel.domain.persistence.entity.Address;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.domain.persistence.entity.Facility;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.test.helpers.FieldValidator;
import org.dcsa.tnt.persistence.entity.AggregatedEquipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedShipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedTransportEvent;
import org.dcsa.tnt.persistence.entity.Reference;
import org.dcsa.tnt.persistence.entity.Seal;
import org.dcsa.tnt.persistence.entity.Service;
import org.dcsa.tnt.persistence.entity.TransportCall;
import org.dcsa.tnt.persistence.entity.Vessel;
import org.dcsa.tnt.persistence.entity.Voyage;
import org.dcsa.tnt.transferobjects.AddressTO;
import org.dcsa.tnt.transferobjects.CarrierTO;
import org.dcsa.tnt.transferobjects.EquipmentEventTO;
import org.dcsa.tnt.transferobjects.FacilityTO;
import org.dcsa.tnt.transferobjects.LocationTO;
import org.dcsa.tnt.transferobjects.ReferenceTO;
import org.dcsa.tnt.transferobjects.SealTO;
import org.dcsa.tnt.transferobjects.ServiceTO;
import org.dcsa.tnt.transferobjects.ShipmentEventTO;
import org.dcsa.tnt.transferobjects.TransportCallTO;
import org.dcsa.tnt.transferobjects.TransportEventTO;
import org.dcsa.tnt.transferobjects.VesselTO;
import org.dcsa.tnt.transferobjects.VoyageTO;
import org.junit.jupiter.api.Test;

public class EventFieldsTest {
  @Test
  public void testShipmentEventFields() {
    FieldValidator.assertFieldsAreEqual(AggregatedShipmentEvent.class, ShipmentEventTO.class,
      // Filled out separately
      "references");
  }

  @Test
  public void testTransportEventFields() {
    FieldValidator.assertFieldsAreEqual(AggregatedTransportEvent.class, TransportEventTO.class,
      // Filled out separately
      "references", "documentReferences");
  }

  @Test
  public void testEquipmentEventFields() {
    FieldValidator.assertFieldsAreEqual(AggregatedEquipmentEvent.class, EquipmentEventTO.class,
      // Filled out separately
      "references", "documentReferences", "seals");
  }

  @Test
  public void testAddressFields() {
    FieldValidator.assertFieldsAreEqual(Address.class, AddressTO.class,
      // Not mapped
      "id"
      );
  }

  @Test
  public void testCarrierFields() {
    FieldValidator.assertFieldsAreEqual(Carrier.class, CarrierTO.class,
      // Not mapped
      "id"
    );
  }

  @Test
  public void testFacilityFields() {
    FieldValidator.assertFieldsAreEqual(Facility.class, FacilityTO.class,
      // Not mapped
      "id"
    );
  }

  @Test
  public void testLocationFields() {
    FieldValidator.assertFieldsAreEqual(Location.class, LocationTO.class,
      // Not mapped
      "id"
    );
  }

  @Test
  public void testReferenceFields() {
    FieldValidator.assertFieldsAreEqual(Reference.class, ReferenceTO.class,
      // Not mapped
      "id"
    );
  }

  @Test
  public void testSealFields() {
    FieldValidator.assertFieldsAreEqual(Seal.class, SealTO.class,
      // Not mapped
      "id"
    );
  }

  @Test
  public void testServiceFields() {
    FieldValidator.assertFieldsAreEqual(Service.class, ServiceTO.class
    );
  }

  @Test
  public void testTransportCallFields() {
    FieldValidator.assertFieldsAreEqual(TransportCall.class, TransportCallTO.class
    );
  }

  @Test
  public void testVesselFields() {
    FieldValidator.assertFieldsAreEqual(Vessel.class, VesselTO.class
    );
  }

  @Test
  public void testVoyageFields() {
    FieldValidator.assertFieldsAreEqual(Voyage.class, VoyageTO.class
    );
  }
}
