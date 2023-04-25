package org.dcsa.tnt.transferobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dcsa.skernel.infrastructure.validation.UniversalServiceReference;
import org.dcsa.skernel.infrastructure.validation.ValidVesselIMONumber;
import org.dcsa.tnt.transferobjects.enums.DocumentTypeCode;
import org.dcsa.tnt.transferobjects.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.EventType;
import org.dcsa.tnt.transferobjects.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.TransportEventTypeCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EventSubscriptionTO {
  @NotBlank
  private String callbackUrl;

  @Size(max = 100)
  private String documentReference;

  @Size(max = 15)
  private String equipmentReference;

  @Size(max = 100)
  private String transportCallReference;

  @ValidVesselIMONumber(allowNull = true)
  private String vesselIMONumber;

  @Size(max = 50)
  private String carrierExportVoyageNumber;

  @Pattern(regexp = "\\d{2}[0-9A-Z]{2}[NEWS]", message = "Not a valid voyage reference")
  private String universalExportVoyageReference;

  @Size(max = 11)
  private String carrierServiceCode;

  @UniversalServiceReference
  private String universalServiceReference;

  @Size(max = 5)
  private String UNLocationCode;

  private Set<EventType> eventTypes;
  private Set<ShipmentEventTypeCode> shipmentEventTypeCodes;
  private Set<DocumentTypeCode> documentTypeCodes;
  private Set<TransportEventTypeCode> transportEventTypeCodes;
  private Set<EquipmentEventTypeCode> equipmentEventTypeCodes;
}
