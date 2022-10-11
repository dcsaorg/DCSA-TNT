package org.dcsa.tnt.service.mapping.transferobject;

import org.dcsa.tnt.service.domain.TransportCall;
import org.dcsa.tnt.transferobjects.TransportCallTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocationMapper.class)
public interface TransportCallMapper {
  @Mapping(target = "carrierImportVoyageNumber", source = "importVoyage.carrierVoyageNumber")
  @Mapping(target = "carrierExportVoyageNumber", source = "exportVoyage.carrierVoyageNumber")
  @Mapping(target = "universalExportVoyageReference", source = "exportVoyage.universalVoyageReference")
  @Mapping(target = "universalImportVoyageReference", source = "importVoyage.universalVoyageReference")
  @Mapping(target = "carrierServiceCode", source = "exportVoyage.service.carrierServiceCode")
  @Mapping(target = "universalServiceReference", source = "importVoyage.service.universalServiceReference")
  TransportCallTO toDTO(TransportCall transportCall);
}
