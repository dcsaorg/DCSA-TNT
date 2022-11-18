package org.dcsa.tnt.service.mapping;

import org.dcsa.skernel.domain.persistence.entity.enums.DimensionUnit;
import org.dcsa.skernel.domain.persistence.entity.enums.*;
import org.dcsa.skernel.test.helpers.EnumValidator;
import org.dcsa.tnt.persistence.entity.enums.*;
import org.junit.jupiter.api.Test;

public class EnumValuesTest {
  @Test
  public void testDocumentReferenceType() {
    EnumValidator.assertHaveSameValues(DocumentReferenceType.class, org.dcsa.tnt.transferobjects.enums.DocumentReferenceType.class);
  }

  @Test
  public void testDimensionUnit() {
    EnumValidator.assertHaveSameValues(DimensionUnit.class, org.dcsa.tnt.transferobjects.enums.DimensionUnit.class);
  }

  @Test
  public void testDocumentTypeCode() {
    EnumValidator.assertHaveSameValues(DocumentTypeCode.class, org.dcsa.tnt.transferobjects.enums.DocumentTypeCode.class);
  }

  @Test
  public void testEmptyIndicatorCode() {
    EnumValidator.assertHaveSameValues(EmptyIndicatorCode.class, org.dcsa.tnt.transferobjects.enums.EmptyIndicatorCode.class);
  }

  @Test
  public void testEquipmentEventTypeCode() {
    EnumValidator.assertHaveSameValues(EquipmentEventTypeCode.class, org.dcsa.tnt.transferobjects.enums.EquipmentEventTypeCode.class);
  }

  @Test
  public void testEventClassifierCode() {
    EnumValidator.assertHaveSameValues(EventClassifierCode.class, org.dcsa.tnt.transferobjects.enums.EventClassifierCode.class);
  }

  @Test
  public void testEventType() {
    EnumValidator.assertHaveSameValues(EventType.class, org.dcsa.tnt.transferobjects.enums.EventType.class);
  }

  @Test
  public void testFacilityCodeListProvider() {
    EnumValidator.assertHaveSameValues(FacilityCodeListProvider.class, org.dcsa.skernel.infrastructure.transferobject.enums.FacilityCodeListProvider.class);
  }

  @Test
  public void testFacilityTypeCode() {
    EnumValidator.assertHaveSameValues(FacilityTypeCode.class, org.dcsa.tnt.transferobjects.enums.FacilityTypeCode.class);
  }

  @Test
  public void testPortCallStatusCode() {
    EnumValidator.assertHaveSameValues(PortCallStatusCode.class, org.dcsa.tnt.transferobjects.enums.PortCallStatusCode.class);
  }

  @Test
  public void testReferenceType() {
    EnumValidator.assertHaveSameValues(ReferenceType.class, org.dcsa.tnt.transferobjects.enums.ReferenceType.class);
  }

  @Test
  public void testSealSourceCode() {
    EnumValidator.assertHaveSameValues(SealSourceCode.class, org.dcsa.tnt.transferobjects.enums.SealSourceCode.class);
  }

  @Test
  public void testSealType() {
    EnumValidator.assertHaveSameValues(SealType.class, org.dcsa.tnt.transferobjects.enums.SealType.class);
  }

  @Test
  public void testShipmentEventTypeCode() {
    EnumValidator.assertHaveSameValues(ShipmentEventTypeCode.class, org.dcsa.tnt.transferobjects.enums.ShipmentEventTypeCode.class);
  }

  @Test
  public void testTransportEventTypeCode() {
    EnumValidator.assertHaveSameValues(TransportEventTypeCode.class, org.dcsa.tnt.transferobjects.enums.TransportEventTypeCode.class);
  }

  @Test
  public void testVesselType() {
    EnumValidator.assertHaveSameValues(VesselType.class, org.dcsa.tnt.transferobjects.enums.VesselType.class);
  }
}
