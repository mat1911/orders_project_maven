package converters;


import model.Customer;

import java.util.List;

public class CustomersJsonConverter extends JsonConverter<List<Customer>> {
    public CustomersJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
