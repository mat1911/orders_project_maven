package validators;

import model.Order;
import org.apache.commons.validator.routines.EmailValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class OrderValidator {
    private Map<String, String> errors;

    public OrderValidator() {
        this.errors = new HashMap<>();
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public Map<String, String> validate(Order order){

        errors.clear();

        if(!isCustomerValid(order)){
            errors.put("customer", "not valid: " + order.getCustomer());
        }

        if(!isProductValid(order)){
            errors.put("product", "not valid: " + order.getProduct());
        }

        if(!isCustomerNameValid(order)){
            errors.put("customer name", "not valid: " + order.getCustomer().getName());
        }

        if(!isCustomerSurnameValid(order)){
            errors.put("customer surname", "not valid: " + order.getCustomer().getSurname());
        }

        if(!isCustomerAgeValid(order)){
            errors.put("customer age", "not valid: " + order.getCustomer().getAge());
        }

        if(!isCustomerMailValid(order)){
            errors.put("customer email", "not valid: " + order.getCustomer().getEmail());
        }

        if(!isProductNameValid(order)){
            errors.put("product name", "not valid: " + order.getProduct().getName());
        }

        if(!isProductPriceValid(order)){
            errors.put("product price", "not valid: " + order.getProduct().getPrice());
        }

        if(!isQuantityValid(order)){
            errors.put("quantity", "not valid: " + order.getQuantity());
        }

        if(!isOrderDateValid(order)){
            errors.put("order date", "not valid: " + order.getOrderDate());
        }

        return errors;
    }

    private boolean isCustomerValid(Order order){
        if(order.getCustomer() == null)
            return false;
        return true;
    }

    private boolean isProductValid(Order order){
        if(order.getProduct() == null)
            return false;
        return true;
    }

    private boolean isCustomerNameValid(Order order){
        if(!isCustomerValid(order))
            return false;
        return order.getCustomer().getName().matches("[A-Z ]+");
    }

    private boolean isCustomerSurnameValid(Order order){
        if(!isCustomerValid(order))
            return false;
        return order.getCustomer().getSurname().matches("[A-Z ]+");
    }

    private boolean isCustomerAgeValid(Order order){
        if(!isCustomerValid(order))
            return false;
        return order.getCustomer().getAge() >= 18;
    }

    private boolean isCustomerMailValid(Order order){
        if(!isCustomerValid(order)) {
            return false;
        }

        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(order.getCustomer().getEmail());
    }

    private boolean isProductNameValid(Order order){
        if(!isProductValid(order))
            return false;
        return order.getProduct().getName().matches("[A-Z ]+");
    }

    private boolean isProductPriceValid(Order order){
        if(!isProductValid(order))
            return false;
        return order.getProduct().getPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isQuantityValid(Order order){
        return order.getQuantity() > 0;
    }

    private boolean isOrderDateValid(Order order){
        return order.getOrderDate().isAfter(LocalDate.now()) || order.getOrderDate().isEqual(LocalDate.now());
    }

}
