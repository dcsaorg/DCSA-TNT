package org.dcsa.tnt.service.mapping.transferobject;

import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.tnt.persistence.entity.enums.DCSATransportType;
import org.dcsa.tnt.service.domain.TransportCall;
import org.dcsa.tnt.transferobjects.ModeOfTransport;
import org.dcsa.tnt.transferobjects.TransportCallTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;

@Mapper(
  componentModel = "spring",
  uses = {
    LocationTOMapper.class,
    VesselMapper.class
  }
)
public interface TransportCallMapper {
  @Mapping(target = "carrierImportVoyageNumber", source = "importVoyage.carrierVoyageNumber")
  @Mapping(target = "carrierExportVoyageNumber", source = "exportVoyage.carrierVoyageNumber")
  @Mapping(target = "universalExportVoyageReference", source = "exportVoyage.universalVoyageReference")
  @Mapping(target = "universalImportVoyageReference", source = "importVoyage.universalVoyageReference")
  @Mapping(target = "carrierServiceCode", source = "exportVoyage.service.carrierServiceCode")
  @Mapping(target = "universalServiceReference", source = "importVoyage.service.universalServiceReference")
  @Mapping(target = "modeOfTransport",source = "modeOfTransportCode", qualifiedByName = "modeOfTransportCodetoTO")
  TransportCallTO toDTO(TransportCall transportCall);
  @Named("modeOfTransportCodetoTO")
  default ModeOfTransport modeOfTransportCodetoTO(String modeOfTransportCode) {
    return Arrays.stream(DCSATransportType.values())
      .filter(t -> t.getCode().toString().equals(modeOfTransportCode))
      .findFirst()
      .map(e -> ModeOfTransport.valueOf(e.name()))
      .orElseThrow(() -> ConcreteRequestErrorMessageException.invalidInput(modeOfTransportCode + " is not a valid transport code"));
  }

}
