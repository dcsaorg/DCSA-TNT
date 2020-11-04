package org.dcsa.tnt.util;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.dcsa.core.exception.GetException;
import org.dcsa.core.extendedrequest.ExtendedParameters;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.core.extendedrequest.FilterItem;
import org.dcsa.core.extendedrequest.Join;
import org.dcsa.core.util.ReflectUtility;
import org.dcsa.tnt.model.Event;
import org.dcsa.tnt.model.Shipment;
import org.dcsa.tnt.model.ShipmentEvent;
import org.springframework.data.relational.core.mapping.Table;

import javax.el.MethodNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A class to handle the fact that an Event can have multiple subEvents (Transport, Equipment, Shipment). Used primarily
 * when converting between JSON property names, POJO field names and DB column names. Also handles BillOfLading filtering
 * as this requires a Join with the Shipment table
 */
public class ExtendedEventRequest extends ExtendedRequest<Event> {
    private final Class<Event>[] modelSubClasses;

    public ExtendedEventRequest(ExtendedParameters extendedParameters, Class<Event>[] modelSubClasses) {
        super(extendedParameters, Event.class);
        this.modelSubClasses = modelSubClasses;
    }

    /**
     * A method to convert a JSON name to a field. It will look through all the modelClasses of this ExtendedEventRequest
     * @param jsonName the JSON name to convert
     * @return the field name corresponding to the JSON name provided
     * @throws NoSuchFieldException if the JSON name is not found on any of the modelClasses defined
     */
    @Override
    public String transformFromJsonNameToFieldName(String jsonName) throws NoSuchFieldException {
        // Run through all possible subClasses and see if one of them can transform the JSON name to a field name
        for (Class<Event> clazz : modelSubClasses) {
            try {
                // Verify that the field exists on the model class and transform it from JSON-name to FieldName
                return ReflectUtility.transformFromJsonNameToFieldName(clazz, jsonName);
            } catch (NoSuchFieldException noSuchFieldException) {
                // Do nothing - try the next sub class
            }
        }
        throw new NoSuchFieldException("Field: " + jsonName + " does not exist on any of: " + getModelClassNames());
    }

    private static final String BILL_OF_LADING_PARAMETER = "billOfLading";

    /**
     * A method to handle parameters that cannot be handled automatically. These parameters do not exist in the event
     * tables and therefore a JOIN is needed
     * @param parameter the parameter to handle
     * @param value the value of the parameter to handle
     * @param fromCursor is this part of a cursorPagination link
     * @return true if the parameter was handled, false if the parameter is not recognised
     */
    @Override
    protected boolean doJoin(String parameter, String value, boolean fromCursor) {
        try {
            String billOfLadingParameter = ReflectUtility.transformFromFieldNameToJsonName(Shipment.class, BILL_OF_LADING_PARAMETER);
            if (billOfLadingParameter.equals(parameter)) {
                // Bill of Lading parameter
                join = new Join();

                Table shipmentTable = Shipment.class.getAnnotation(Table.class);
                if (shipmentTable == null) {
                    throw new GetException("@Table not defined on Shipment-class!");
                }

                String shipmentShipmentIdColumn = ReflectUtility.transformFromFieldNameToColumnName(Shipment.class, "id");
                String shipmentEventShipmentIdColumn = ReflectUtility.transformFromFieldNameToColumnName(ShipmentEvent.class, "shipmentId");
                join.add(shipmentTable.value() + " ON " + shipmentTable.value() + "." + shipmentShipmentIdColumn + " = " + getTableName() + "." + shipmentEventShipmentIdColumn);
                filter.addFilterItem(new FilterItem(BILL_OF_LADING_PARAMETER, null, Shipment.class, value, true, false, true, true, filter.getNewBindCounter()));
                return true;
            }
            return false;
        } catch (NoSuchFieldException noSuchFieldException) {
            return false;
        }
    }

    public String getTableName() {
        StringBuilder sb = new StringBuilder();
        getTableName(sb);
        return sb.toString();
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

    @Override
    public String transformFromFieldNameToColumnName(String fieldName) throws NoSuchFieldException {
        // Run through all possible subClasses and see if one of them can transform the fieldName name to a column name
        for (Class<Event> clazz : modelSubClasses) {
            try {
                // Verify that the field exists on the model class and transform it from JSON-name to FieldName
                return ReflectUtility.transformFromFieldNameToColumnName(clazz, fieldName);
            } catch (NoSuchFieldException noSuchFieldException) {
                // Do nothing - try the next sub class
            }
        }
        throw new NoSuchFieldException("Field: " + fieldName + " does not exist on any of: " + getModelClassNames());
    }

    @Override
    protected String transformFromFieldNameToJsonName(String fieldName) throws NoSuchFieldException {
        // Run through all possible subClasses and see if one of them can transform the fieldName name to a json name
        for (Class<Event> clazz : modelSubClasses) {
            try {
                // Verify that the field exists on the model class and transform it from FieldName to JSON-name
                return ReflectUtility.transformFromFieldNameToJsonName(clazz, fieldName);
            } catch (NoSuchFieldException noSuchFieldException) {
                // Do nothing - try the next sub class
            }
        }
        throw new NoSuchFieldException("Field: " + fieldName + " does not exist on any of: " + getModelClassNames());
    }

    private String getModelClassNames() {
        StringBuilder sb = new StringBuilder();
        for (Class<Event> clazz : modelSubClasses) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(clazz.getSimpleName());
        }
        return sb.toString();
    }

    @Override
    protected Class<?> getFieldType(String fieldName) throws NoSuchFieldException {
        // Run through all possible subClasses
        for (Class<Event> clazz : modelSubClasses) {
            try {
                // Investigate if the return type of the getter method corresponding to fieldName is an Enum
                return ReflectUtility.getFieldType(clazz, fieldName);
            } catch (MethodNotFoundException methodNotFoundException) {
                // Do nothing - try the next sub class
            }
        }
        throw new MethodNotFoundException("No getter method found for field: " + fieldName + " tested the following subclasses: " + getModelClassNames());
    }
}
