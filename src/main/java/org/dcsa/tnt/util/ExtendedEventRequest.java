package org.dcsa.tnt.util;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.SneakyThrows;
import org.dcsa.core.extendedrequest.ExtendedParameters;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.core.extendedrequest.QueryField;
import org.dcsa.core.extendedrequest.QueryFields;
import org.dcsa.core.query.DBEntityAnalysis;
import org.dcsa.core.util.ReflectUtility;
import org.dcsa.tnt.model.Event;
import org.dcsa.tnt.model.ShipmentEvent;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.relational.core.sql.Join;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.relational.core.sql.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * A class to handle the fact that an Event can have multiple subEvents (Transport, Equipment, Shipment). Used primarily
 * when converting between JSON property names, POJO field names and DB column names. Also handles BillOfLading filtering
 * as this requires a Join with the Shipment table
 */
public class ExtendedEventRequest extends ExtendedRequest<Event> {

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

    private static final String EVENT_TYPE_FIELD_NAME;
    private static final Map<String, Constructor<? extends Event>> NAME2CONSTRUCTOR;
    private static final Set<Class<? extends Event>> KNOWN_EVENT_CLASSES;

    static {
        JsonSubTypes jsonSubTypes = Event.class.getAnnotation(JsonSubTypes.class);
        JsonTypeInfo jsonTypeInfo = Event.class.getAnnotation(JsonTypeInfo.class);
        if (jsonSubTypes != null && jsonTypeInfo != null) {
            String property = jsonTypeInfo.property();
            // The discriminator value is on the Event class
            try {
                EVENT_TYPE_FIELD_NAME = ReflectUtility.transformFromFieldNameToColumnName(Event.class, property);
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException("Event MUST have the field " + property + " (listed in @JsonTypeInfo)");
            }
            NAME2CONSTRUCTOR = new HashMap<>();
            KNOWN_EVENT_CLASSES = new HashSet<>();
            for (JsonSubTypes.Type type : jsonSubTypes.value()) {
                String value = type.name();
                Class<?> rawClass = type.value();
                if (!Event.class.isAssignableFrom(rawClass)) {
                    throw new IllegalStateException(rawClass.getSimpleName()
                            + " (mentioned in JsonSubTypes of Event.class) was not a subclass of Event");
                }
                @SuppressWarnings({"unchecked"})
                Class<? extends Event> eventClass = (Class<? extends Event>)rawClass;
                KNOWN_EVENT_CLASSES.add(eventClass);
                try {
                    Constructor<? extends Event> constructor = eventClass.getDeclaredConstructor();
                    if (!Modifier.isPublic(constructor.getModifiers())) {
                        throw new IllegalStateException("The no-argument constructor for " + eventClass.getSimpleName()
                                + " is not public but it must be.  The class is listed as a subclass in"
                                + " @JsonSubTypes on the Event class.");
                    }
                    NAME2CONSTRUCTOR.put(value, constructor);
                } catch (NoSuchMethodException e) {
                    throw new IllegalStateException("The event subclass " + eventClass.getSimpleName()
                            + " MUST have a no-argument constructor.  The class is listed as a subclass in"
                            + " @JsonSubTypes on the Event class.");
                }

            }
        } else {
            throw new IllegalStateException("Event MUST have a @JsonSubTypes and @JsonTypeInfo");
        }
    }

    private final Iterable<Class<? extends Event>> modelSubClasses;

    public ExtendedEventRequest(ExtendedParameters extendedParameters, R2dbcDialect r2dbcDialect) {
        this(extendedParameters, r2dbcDialect, KNOWN_EVENT_CLASSES);
    }

    public ExtendedEventRequest(ExtendedParameters extendedParameters, R2dbcDialect r2dbcDialect, Iterable<Class<? extends Event>> modelSubClasses) {
        super(extendedParameters, r2dbcDialect, Event.class);
        this.modelSubClasses = modelSubClasses;
        for (Class<?> clazz : modelSubClasses) {
            if (!KNOWN_EVENT_CLASSES.contains(clazz)) {
                throw new IllegalStateException("Unsupported class " + clazz.getSimpleName()
                        + " - it is not registered in @JsonSubTypes of Event");
            }
        }
    }

    @SneakyThrows({NoSuchFieldException.class})
    protected DBEntityAnalysis.DBEntityAnalysisBuilder<Event> prepareDBEntityAnalysis() {
        DBEntityAnalysis.DBEntityAnalysisBuilder<Event> builder = super.prepareDBEntityAnalysis();
        Table eventTable = builder.getPrimaryModelTable();
        Set<String> seen = new HashSet<>();
        String shipmentEventShipmentIdColumn = ReflectUtility.transformFromFieldNameToColumnName(ShipmentEvent.class, "shipmentId");
        Table shipmentTable = Table.create(SHIPMENT_TABLE_NAME);
        Table shipmentEquipmentTable = Table.create(SHIPMENT_EQUIPMENT_TABLE_NAME);
        Table cargoItemTable = Table.create(CARGO_ITEM_TABLE_NAME);
        Table shippingInstructionsTable = Table.create(SHIPPING_INSTRUCTION_TABLE_NAME);
        Table transportDocumentTable = Table.create(TRANSPORT_DOCUMENT_TABLE_NAME);
        boolean includesShipmentEvents = false;


        for (Class<?> clazz : modelSubClasses) {
            Class<?> currentClass = clazz;
            if (ShipmentEvent.class.isAssignableFrom(currentClass)) {
                includesShipmentEvents = true;
            }
            while (currentClass != Event.class) {
                for (Field field : currentClass.getDeclaredFields()) {
                    QueryField queryField = QueryFields.queryFieldFromField(Event.class, field, clazz, eventTable, true);
                    if (seen.add(queryField.getJsonName())) {
                        builder = builder.registerQueryField(queryField);
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        if (includesShipmentEvents) {
            builder = builder
                    .join(Join.JoinType.JOIN, eventTable, shipmentTable)
                    .onEqualsThen(shipmentEventShipmentIdColumn, SHIPMENT_TABLE_ID_COLUMN_NAME)
                    .chainJoin(shipmentEquipmentTable)
                    .onEqualsThen(SHIPMENT_TABLE_ID_COLUMN_NAME, SHIPMENT_EQUIPMENT_SHIPMENT_ID_COLUMN_NAME)
                    .chainJoin(cargoItemTable)
                    .onEqualsThen(SHIPMENT_EQUIPMENT_ID_COLUMN_NAME, CARGO_ITEM_TABLE_SHIPMENT_EQUIPMENT_ID_COLUMN_NAME)
                    .chainJoin(shippingInstructionsTable)
                    .onEqualsThen(CARGO_ITEM_TABLE_SHIPPING_INSTRUCTION_ID_COLUMN_NAME, SHIPPING_INSTRUCTION_ID_COLUMN_NAME)
                    .chainJoin(transportDocumentTable)
                    .onEqualsThen(SHIPPING_INSTRUCTION_ID_COLUMN_NAME, TRANSPORT_DOCUMENT_TABLE_SHIPPING_INSTRUCTION_ID_COLUMN_NAME)
                    .registerQueryField(
                            SqlIdentifier.unquoted(TRANSPORT_DOCUMENT_TABLE_ID_COLUMN_NAME),
                            TRANSPORT_DOCUMENT_ID_JSON_NAME,
                            UUID.class
                    );
        }
        return builder;
    }

    @Override
    protected void markQueryFieldInUse(QueryField fieldInUse) {
        super.markQueryFieldInUse(fieldInUse);
        if (JSON_FIELDS_REQUIRING_DISTINCT.contains(fieldInUse.getJsonName())) {
            this.selectDistinct = true;
        }
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
    @SneakyThrows({InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class})
    @Override
    public Event getModelClassInstance(Row row, RowMetadata meta) {
        Object value = row.get(EVENT_TYPE_FIELD_NAME);
        Constructor<? extends Event> constructor;
        if (value instanceof String) {
            constructor = NAME2CONSTRUCTOR.get(value);
        } else {
            constructor = null;
        }
        if (constructor == null) {
            throw new IllegalStateException("Unknown Event type (field: " + EVENT_TYPE_FIELD_NAME + "), got value: "
                    + value);
        }
        return constructor.newInstance();
    }

}
