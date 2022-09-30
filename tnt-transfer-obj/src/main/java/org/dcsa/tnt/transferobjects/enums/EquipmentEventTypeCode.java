package org.dcsa.tnt.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EquipmentEventTypeCode {
  LOAD("The action of lifting cargo or a container on board of the mode of transportation. Load is complete once the cargo or container has been lifted on board the mode of transport and secured."),
  DISC("The action of lifting cargo or containers off a mode of transport. Discharge is the opposite of load."),
  GTIN("The action when a container is introduced into a controlled area like a port - or inland terminal. Gate in has been completed once the operator of the area is legally in possession of the container."),
  GTOT("The action when a container is removed from a controlled area like a port – or inland terminal. Gate-out has been completed once the possession of the container has been transferred from the operator of the terminal to the entity who is picking up the container."),
  STUF("The process of loading the cargo in a container or in/onto another piece of equipment."),
  STRP("The action of unloading cargo from containers or equipment."),
  PICK("The action of collecting the container at customer location."),
  DROP("The action of delivering the container at customer location."),
  INSP("Identifies that the seal on equipment has been inspected."),
  RSEA("Identifies that the equipment has been resealed after inspection."),
  RMVD("Identifies that a Seal has been removed from the equipment for inspection."),
  AVPU("Available for Pick-up"),
  AVDO("Available for Drop-off"),
  CUSS("Customs Selected for Scan"),
  CUSI("Customs Selected for Inspection"),
  CUSR("Customs Released")
  ;

  @Getter
  private String description;
}
