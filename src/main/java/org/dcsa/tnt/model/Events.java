package org.dcsa.tnt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class Events {

    private List<Event> events;

    public Events(Event event){
        events = Arrays.asList(event);
    }

    public Events(List<Event> events) {
        this.events = events;
    }
}
