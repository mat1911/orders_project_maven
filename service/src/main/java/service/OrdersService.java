package service;

import converters.CustomersJsonConverter;
import converters.OrdersJsonConverter;
import exceptions.AppException;
import model.Customer;
import model.Order;
import model.Product;
import model.enums.Category;
import org.eclipse.collections.impl.collector.BigDecimalSummaryStatistics;
import org.eclipse.collections.impl.collector.Collectors2;
import validators.OrderValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class OrdersService {
    private List<Order> orders;

    public OrdersService() { orders = takeOrdersFromUser(3); }

    public OrdersService(String jsonFileName) {
        orders = new ArrayList<>();
        orders = takeOrdersFromFile(jsonFileName);
    }

    private List<Order> takeOrdersFromUser(int numberOfOrders){
        UserDataService userDataService = new UserDataService();
        return userDataService.takeOrdersFormUser(numberOfOrders);
    }


    private List<Order> takeOrdersFromFile(String jsonFileName) {
        OrderValidator orderValidator = new OrderValidator();
        OrdersJsonConverter ordersJsonConverter = new OrdersJsonConverter(jsonFileName);
        List<Order> orderList;

        orderList = ordersJsonConverter
                .fromJson()
                .orElseThrow(() -> new AppException("ORDERS SERVICE - GET ORDERS FROM JASON FILE"));

        orderList.forEach(orderValidator::validate);

        if(orderValidator.hasErrors()){
            throw new AppException("ORDER SERVICE - ORDERS FROM FILE ARE NOT VALID");
        }

        return orderList;
    }

    public BigDecimal averagePriceBetween(LocalDate from, LocalDate to){
        if(from.isAfter(to))
            throw new AppException("DATE FROM SHOULD BY BEFORE DATE TO");

        BigDecimalSummaryStatistics statistics = orders.stream()
                .filter(x -> x.getOrderDate().isAfter(from.minusDays(1)) && x.getOrderDate().isBefore(to.plusDays(1)))
                .peek(x -> x.getProduct().setPrice(
                        x.getProduct().getPrice().multiply(new BigDecimal(x.getQuantity()))
                ))
                .collect(Collectors2.summarizingBigDecimal(x -> x.getProduct().getPrice()));

        return statistics.getAverage();
    }

    public Map<Category, Product> productWithHighestPrice(){
         return orders.stream()
                 .collect(Collectors.groupingBy(order -> order.getProduct().getCategory(), Collectors.maxBy(Comparator.comparing(order -> order.getProduct().getPrice()))))
                 .entrySet()
                 .stream()
                 .collect(
                         Collectors.toMap(Map.Entry::getKey,
                                 order -> order.getValue()
                                         .orElseThrow(() -> new AppException("NO ELEMENTS FOUND"))
                                         .getProduct())
                 );
    }

    public LocalDate[] datesWithHighestAndLowestSales(){ // array[0] -> date with lowest sales, array[1] -> date with highest sales

        LocalDate[] result = new LocalDate[2];

        Map<LocalDate, Long> groupedDates = orders.stream()
                .map(x -> x.getOrderDate())
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()));

        LongSummaryStatistics stats = groupedDates.entrySet()
                .stream()
                .collect(Collectors.summarizingLong(x -> x.getValue()));

        result[1] = groupedDates.entrySet().stream()
                .sorted((x, y) -> (int) (x.getValue() - y.getValue()))
                .max(Comparator.comparingInt(x -> x.getValue().intValue()))
                .get().getKey();

        result[0] = groupedDates.entrySet().stream()
                .sorted((x, y) -> (int) (x.getValue() - y.getValue()))
                .min(Comparator.comparingInt(x -> x.getValue().intValue()))
                .get().getKey();

        return result;
    }

    public Customer personWhoPaidTheHighestPrice(){
        return orders.stream()
                .max(Comparator.comparing(x -> x.getProduct().getPrice().multiply(new BigDecimal(x.getQuantity()))))
                .map(x -> x.getCustomer())
                .get();
    }

    public BigDecimal totalIncomeWithDiscounts(){
        final BigDecimal PRICE_TO_PAY_FOR_YOUNGER_THAN_25 = new BigDecimal(0.97);
        final BigDecimal PRICE_FOR_ORDER_DATE_EARLIER_THAN_2 = new BigDecimal(0.98);

        BigDecimalSummaryStatistics stat = orders.stream()
                .peek(x -> {
                    Period per = Period.between(LocalDate.now(), x.getOrderDate());
                    if(x.getCustomer().getAge() < 25)
                        x.getProduct().setPrice(x.getProduct().getPrice().multiply(PRICE_TO_PAY_FOR_YOUNGER_THAN_25));
                    else if(per.getDays() <= 2)
                        x.getProduct().setPrice(x.getProduct().getPrice().multiply(PRICE_FOR_ORDER_DATE_EARLIER_THAN_2));
                })
                .peek(x -> x.getProduct().getPrice().multiply(new BigDecimal(x.getQuantity())))
                .collect(Collectors2.summarizingBigDecimal(x -> x.getProduct().getPrice()));

        return stat.getSum();
    }

    public int numberOfCustomersWithQuantityOfProduct(int minQuantity){
        CustomersJsonConverter customersJsonConverter = new CustomersJsonConverter("resources/customers.json");
        List<Customer> customers = orders.stream()
                .filter(x -> x.getQuantity() >= minQuantity)
                .map(x -> x.getCustomer())
                .collect(Collectors.toList());

        System.out.println("Saving information to file customers.json");
        customersJsonConverter.toJson(customers);

        return customers.size();
    }

    public Category categoryWithHighestSales(){
        Map<Category, Long> countedCategories=  orders.stream()
                .collect(Collectors.groupingBy(x -> x.getProduct().getCategory(), Collectors.summingLong(x -> x.getQuantity())));

        return countedCategories.entrySet().stream()
                .max((x, y) -> (int) (x.getValue() - y.getValue()))
                .get().getKey();
    }

    public Map<Month, Integer> quantityOfSoldProductPerMonth(){
        Map<Month, Integer> numberOfProductsPerMonth = orders.stream()
                .collect(Collectors.groupingBy(x -> x.getOrderDate().getMonth(), Collectors.summingInt(x -> x.getQuantity())));

        return numberOfProductsPerMonth.entrySet().stream()
                .sorted(Comparator.comparing(x -> x.getValue(), Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new));
    }

    public Map<Month, Category> bestSellingCategoryPerMonth(){
        Map<Month, Map<Category, Long>> numberOfSoldCategoriesPerMonth = orders.stream()
                .collect(Collectors.groupingBy(x -> x.getOrderDate().getMonth(), Collectors.groupingBy(x -> x.getProduct().getCategory(), Collectors.counting())));

        return numberOfSoldCategoriesPerMonth.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        x -> x.getKey(),
                        y -> y.getValue().entrySet()
                                .stream()
                                .max((k, v) -> (int) (k.getValue() - v.getValue()))
                                .get()
                                .getKey())
                );
    }

    @Override
    public String toString() {
        return orders.stream().map(this::orderStr).collect(Collectors.joining("\n"));
    }

    private String orderStr(Order order){
        return "CUSTOMER NAME: " + order.getCustomer().getName() + "\n" +
                "CUSTOMER SURNAME: " + order.getCustomer().getSurname() + "\n" +
                "CUSTOMER AGE: " + order.getCustomer().getAge() + "\n" +
                "CUSTOMER EMAIL: " + order.getCustomer().getEmail() + "\n" +
                "PRODUCT NAME: " + order.getProduct().getName() + "\n" +
                "PRODUCT PRICE: " + order.getProduct().getPrice() + "\n" +
                "PRODUCT CATEGORY: " + order.getProduct().getCategory() + "\n" +
                "QUANTITY: " + order.getQuantity() + "\n" +
                "ORDER DATE: " + order.getOrderDate() + "\n";
    }
}
