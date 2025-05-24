package com.vr61v.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr61v.entities.ContactData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class TicketFilter implements Filter {

    private final static ObjectMapper mapper = new ObjectMapper();

    private final String bookRef;
    private final String passengerId;
    private final String passengerName;
    private final ContactData contactData;

    @Override
    public Map<String, Object> toWhereParameters() {
        Map<String, Object> parameters = new HashMap<>();

        if (bookRef != null) {
            parameters.put("book_ref LIKE ?", bookRef);
        }
        if (passengerId != null) {
            parameters.put("passenger_id LIKE ?", passengerId);
        }
        if (passengerName != null) {
            parameters.put("passenger_name LIKE ?", passengerName);
        }
        if (contactData != null) {
            try {
                parameters.put("contact_data @> (to_jsonb(?::json))", mapper.writeValueAsString(contactData));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return parameters;
    }
}
