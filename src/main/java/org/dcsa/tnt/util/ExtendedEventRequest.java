package org.dcsa.tnt.util;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.SneakyThrows;
import org.dcsa.core.exception.GetException;
import org.dcsa.core.extendedrequest.*;
import org.dcsa.core.util.ReflectUtility;
import org.dcsa.tnt.model.*;
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
    private static final String TRANSPORT_DOCUMENT_ID_COLUMN_NAME = "transport_document_id";
    private static final String SCHEDULE_ID_PARAMETER = "id";

    private static final String SHIPMENT_TABLE_NAME = "shipment";
    private static final String SHIPMENT_TABLE_ID_COLUMN_NAME = "id";
    private static final String TRANSPORT_CALL_TABLE_NAME = "transport_call";
    private static final String TRANSPORT_CALL_ID_COLUMN_NAME = "id";
    private static final String TRANSPORT_CALL_SCHEDULE_ID_COLUMN_NAME = "schedule_id";

    @Override
    public Class<?> getPrimaryModelClass() {
        return this.getModelClass();
    }

    @SneakyThrows({NoSuchFieldException.class})
    @Override
    protected void loadFieldsFromSubclass() {
        String tableName = this.getTableName(getPrimaryModelClass());
        String scheduleIdParameter = ReflectUtility.transformFromFieldNameToJsonName(Schedule.class, SCHEDULE_ID_PARAMETER);
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
                SHIPMENT_TABLE_NAME,
                TRANSPORT_DOCUMENT_ID_COLUMN_NAME,
                TRANSPORT_DOCUMENT_ID_JSON_NAME,
                UUID.class
        ));
        registerQueryField(QueryFields.nonSelectableQueryField(
                TRANSPORT_CALL_TABLE_NAME,
                TRANSPORT_CALL_SCHEDULE_ID_COLUMN_NAME,
                scheduleIdParameter,
                UUID.class
        ));
    }

    private JoinDescriptor joinDescriptor(String tableName, String column, String existingJoinAlias, String existingColumn) {
        return SimpleJoinDescriptor.of(
                org.springframework.data.relational.core.sql.Join.JoinType.JOIN,
                tableName,
                tableName,
                "ON " + tableName + "." + column + " = " + existingJoinAlias + "." + existingColumn,
                null
        );
    }

    @SneakyThrows({NoSuchFieldException.class})
    @Override
    protected void findAllTablesAndBuildJoins() {
        String tableName = getTableName(getModelClass());
        super.findAllTablesAndBuildJoins();
        String shipmentEventShipmentIdColumn = ReflectUtility.transformFromFieldNameToColumnName(ShipmentEvent.class, "shipmentId");
        String transportEventTransportCallIdColumn = ReflectUtility.transformFromFieldNameToColumnName(TransportEvent.class, "transportCallID");

        registerJoinDescriptor(joinDescriptor(SHIPMENT_TABLE_NAME, SHIPMENT_TABLE_ID_COLUMN_NAME, tableName, shipmentEventShipmentIdColumn));

        registerJoinDescriptor(joinDescriptor(TRANSPORT_CALL_TABLE_NAME, TRANSPORT_CALL_ID_COLUMN_NAME, tableName, transportEventTransportCallIdColumn));
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
