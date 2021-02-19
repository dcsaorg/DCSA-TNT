package org.dcsa.tnt.util;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.SneakyThrows;
import org.dcsa.core.exception.GetException;
import org.dcsa.core.extendedrequest.*;
import org.dcsa.core.util.ReflectUtility;
import org.dcsa.tnt.model.EquipmentEvent;
import org.dcsa.tnt.model.Event;
import org.dcsa.tnt.model.ShipmentEvent;
import org.dcsa.tnt.model.TransportEvent;
import org.springframework.data.relational.core.mapping.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A class to handle the fact that an Event can have multiple subEvents (Transport, Equipment, Shipment). Used primarily
 * when converting between JSON property names, POJO field names and DB column names. Also handles BillOfLading filtering
 * as this requires a Join with the Shipment table
 */
public class ExtendedEventRequest extends ExtendedRequest<Event> {
    private static final Class<? extends Event>[] MODEL_SUB_CLASSES = new Class[] {
            EquipmentEvent.class,
            ShipmentEvent.class,
            TransportEvent.class,
    };

    public ExtendedEventRequest(ExtendedParameters extendedParameters) {
        super(extendedParameters, Event.class);
    }

    private static final String TRANSPORT_DOCUMENT_ID_JSON_NAME = "transportDocumentID";

    private static final String SHIPMENT_TABLE_NAME = "shipment";
    private static final String SHIPMENT_TABLE_ID_COLUMN_NAME = "id";

    private static final String SHIPMENT_EQUIPMENT_TABLE_NAME = "shipment_equipment";
    private static final String SHIPMENT_EQUIPMENT_ID_COLUMN_NAME = "id";
    private static final String SHIPMENT_EQUIPMENT_SHIPMENT_ID_COLUMN_NAME = "shipment_id";

    private static final String CARGO_ITEM_TABLE_NAME = "cargo_item";
    private static final String CARGO_ITEM_TABLE_SHIPMENT_EQUIPMENT_ID_COLUMN_NAME = "shipment_equipment_id";
    private static final String CARGO_ITEM_TABLE_SHIPPING_INSTRUCTION_ID_COLUMN_NAME = "shipping_instruction_id";

    private static final String SHIPPING_INSTRUCTION_TABLE_NAME = "shipping_instruction";
    private static final String SHIPPING_INSTRUCTION_ID_COLUMN_NAME = "id";

    private static final String TRANSPORT_DOCUMENT_TABLE_NAME = "transport_document";
    private static final String TRANSPORT_DOCUMENT_TABLE_SHIPPING_INSTRUCTION_ID_COLUMN_NAME = "shipping_instruction_id";
    private static final String TRANSPORT_DOCUMENT_TABLE_ID_COLUMN_NAME = "id";

    private static final Set<String> JSON_FIELDS_REQUIRING_DISTINCT = Set.of(TRANSPORT_DOCUMENT_ID_JSON_NAME);

    @Override
    public Class<?> getPrimaryModelClass() {
        return this.getModelClass();
    }

    @Override
    protected void loadFieldsFromSubclass() {
        String tableName = this.getTableName(getPrimaryModelClass());
        Set<String> seen = new HashSet<>();
        super.loadFieldsFromSubclass();

        for (Class<?> clazz : MODEL_SUB_CLASSES) {
            Class<?> currentClass = clazz;
            while (currentClass != Event.class) {
                for (Field field : currentClass.getDeclaredFields()) {
                    QueryField queryField = QueryFields.queryFieldFromField(Event.class, field, clazz, tableName, true);
                    if (seen.add(queryField.getJsonName())) {
                        registerQueryField(queryField);
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
        }

        registerQueryField(QueryFields.nonSelectableQueryField(
                TRANSPORT_DOCUMENT_TABLE_NAME,
                TRANSPORT_DOCUMENT_TABLE_ID_COLUMN_NAME,
                TRANSPORT_DOCUMENT_ID_JSON_NAME,
                UUID.class
        ));
    }

    protected void finishedParsingParameters() {
        for (FilterItem filterItem : filter.getFilters()) {
            if (JSON_FIELDS_REQUIRING_DISTINCT.contains(filterItem.getQueryField().getJsonName())) {
                this.selectDistinct = true;
                break;
            }
        }
    }

    private JoinDescriptor joinDescriptor(String existingJoinAlias, String existingColumn, String tableName, String column) {
        return SimpleJoinDescriptor.of(
                org.springframework.data.relational.core.sql.Join.JoinType.JOIN,
                tableName,
                tableName,
                "ON " + tableName + "." + column + " = " + existingJoinAlias + "." + existingColumn,
                existingJoinAlias
        );
    }

    @SneakyThrows({NoSuchFieldException.class})
    @Override
    protected void findAllTablesAndBuildJoins() {
        String tableName = getTableName(getModelClass());
        super.findAllTablesAndBuildJoins();
        String shipmentEventShipmentIdColumn = ReflectUtility.transformFromFieldNameToColumnName(ShipmentEvent.class, "shipmentId");

        registerJoinDescriptor(joinDescriptor(tableName, shipmentEventShipmentIdColumn, SHIPMENT_TABLE_NAME, SHIPMENT_TABLE_ID_COLUMN_NAME));

        registerJoinDescriptor(joinDescriptor(SHIPMENT_TABLE_NAME, SHIPMENT_TABLE_ID_COLUMN_NAME,
                SHIPMENT_EQUIPMENT_TABLE_NAME, SHIPMENT_EQUIPMENT_SHIPMENT_ID_COLUMN_NAME));

        registerJoinDescriptor(joinDescriptor(SHIPMENT_EQUIPMENT_TABLE_NAME, SHIPMENT_EQUIPMENT_ID_COLUMN_NAME,
                CARGO_ITEM_TABLE_NAME, CARGO_ITEM_TABLE_SHIPMENT_EQUIPMENT_ID_COLUMN_NAME));

        registerJoinDescriptor(joinDescriptor(CARGO_ITEM_TABLE_NAME, CARGO_ITEM_TABLE_SHIPPING_INSTRUCTION_ID_COLUMN_NAME,
                SHIPPING_INSTRUCTION_TABLE_NAME, SHIPPING_INSTRUCTION_ID_COLUMN_NAME));

        registerJoinDescriptor(joinDescriptor(SHIPPING_INSTRUCTION_TABLE_NAME, SHIPPING_INSTRUCTION_ID_COLUMN_NAME,
                TRANSPORT_DOCUMENT_TABLE_NAME, TRANSPORT_DOCUMENT_TABLE_SHIPPING_INSTRUCTION_ID_COLUMN_NAME));
    }

    @Override
    public void getTableName(StringBuilder sb) {
        Table table = Event.class.getAnnotation(Table.class);
        if (table == null) {
            throw new GetException("@Table not defined on Event-class!");
        }
        sb.append(table.value());
    }

    @Override
    public boolean ignoreUnknownProperties() {
        // Always ignore unknown properties when using Event class (the properties are on the sub classes)
        return true;
    }

    /**
     * A method to look at the database row and via reflection determine the type of SubEvent to create. It will look
     * at the Event class and extract the the discriminator value from the row and create a new instance of the SubClass
     * @param row the Database row containing the object to create
     * @param meta the Database metadata about the row
     * @return a Subclassed event (TransportEvent, EquipmentEvent or ShipmentEvent) corresponding to the discriminator
     */
    @Override
    public Event getModelClassInstance(Row row, RowMetadata meta) {
        try {
            JsonSubTypes jsonSubTypes = Event.class.getAnnotation(JsonSubTypes.class);
            JsonTypeInfo jsonTypeInfo = Event.class.getAnnotation(JsonTypeInfo.class);
            if (jsonSubTypes != null && jsonTypeInfo != null) {
                String property = jsonTypeInfo.property();
                // The discriminator value is on the Event class
                String columnName = ReflectUtility.transformFromFieldNameToColumnName(Event.class, property);
                Object value = row.get(columnName);
                for (JsonSubTypes.Type type : jsonSubTypes.value()) {
                    if (type.name().equals(value)) {
                        // Create a new instance of the sub class to Event
                        Constructor<?> constructor = type.value().getDeclaredConstructor();
                        return (Event) constructor.newInstance();
                    }
                }
                throw new GetException("Unmatched sub-type: " + value + " of Event.class");
            } else {
                return super.getModelClassInstance(row, meta);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            throw new GetException("Error when creating a new sub class of Event.class");
        }
    }

}
