package org.dcsa.tnt.service.mapping.transferobject;

import org.dcsa.tnt.service.domain.Carrier;
import org.dcsa.tnt.service.domain.Vessel;
import org.dcsa.tnt.transferobjects.VesselTO;
import org.dcsa.tnt.transferobjects.enums.OperatorCarrierCodeListProvider;
import org.springframework.stereotype.Component;

@Component
public class VesselMapper {
  public VesselTO toDTO(Vessel vessel) {
    if (vessel == null) {
      return null;
    }

    String operatorCarrierCode = null;
    OperatorCarrierCodeListProvider operatorCarrierCodeListProvider = null;
    if (vessel.vesselOperatorCarrier() != null) {
      Carrier carrier = vessel.vesselOperatorCarrier();
      if (carrier.smdgCode() != null) {
        operatorCarrierCode = carrier.smdgCode();
        operatorCarrierCodeListProvider = OperatorCarrierCodeListProvider.SMDG;
      } else if (carrier.nmftaCode() != null) {
        operatorCarrierCode = carrier.nmftaCode();
        operatorCarrierCodeListProvider = OperatorCarrierCodeListProvider.NMFTA;
      } else {
        throw new IllegalArgumentException("Carrier " + carrier.id() + " has neither smdgCode nor nmftaCode");
      }
    }
    return VesselTO.builder()
      .vesselIMONumber(vessel.vesselIMONumber())
      .name(vessel.name())
      .flag(vessel.flag())
      .callSign(vessel.callSignNumber())
      .operatorCarrierCode(operatorCarrierCode)
      .operatorCarrierCodeListProvider(operatorCarrierCodeListProvider)
      .build();
  }
}
