package service;

import exceptions.AppException;
import model.Customer;
import model.Order;
import model.Product;
import model.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDataService {

    private Scanner scanner;

    public UserDataService(){
        scanner = new Scanner(System.in);
    }

    public List<Order> takeOrdersFormUser(int numberOfOrders) {

        List<Order> ordersFromUser = new ArrayList<>();
        UserDataService userDataService = new UserDataService();
        Customer customer;
        Product product;
        Order order;

        for (int i = 0; i < numberOfOrders; i++) {
            ordersFromUser.add(Order
                    .builder()
                    .customer(takeCustomerFromUser())
                    .product(takeProductFromUser())
                    .quantity(userDataService.getInt("ENTER QUANTITY OF PRODUCT"))
                    .orderDate(userDataService.getDate("ENTER ORDER DATE [YYYY-MM-DD]"))
                    .build()
            );
        }
        return ordersFromUser;
    }

    public Customer takeCustomerFromUser() {
        return Customer
                .builder()
                .name(getStringOfLetters("ENTER CUSTOMER NAME", true))
                .surname(getStringOfLetters("ENTER CUSTOMER SURNAME", true))
                .age(getInt("ENTER CUSTOMER AGE"))
                .email(getStringOfLetters("ENTER CUSTOMER EMAIL", false))
                .build();
    }

    public Product takeProductFromUser() {
        return Product
                .builder()
                .name(getStringOfLetters("ENTER PRODUCT NAME", true))
                .category(getCategory())
                .price(getBigDecimal("ENTER PRODUCT PRICE"))
                .build();
    }

    public int getInt(String message) {

        System.out.println(message);
        String text = scanner.nextLine();

        if (!text.matches("\\d+")) {
            throw new AppException("INT VALUE IS NOT CORRECT: " + text);
        }

        return Integer.parseInt(text);

    }

    public double getDouble(String message) {

        System.out.println(message);
        String text = scanner.nextLine();

        if (!text.matches("[0-9]+.*[0-9]+]")) {
            throw new AppException("DOUBLE VALUE IS NOT CORRECT: " + text);
        }

        return Double.parseDouble(text);

    }

    public Category getCategory() {
        Category[] categories = Category.values();

        AtomicInteger counter = new AtomicInteger(1);
        Arrays
                .stream(categories)
                .forEach(productCategory -> System.out.println(counter.getAndIncrement() + ". " + productCategory));
        System.out.println("Enter category number:");
        String text = scanner.nextLine();

        if (!text.matches("[1-" + categories.length + "]")) {
            throw new AppException("Category value is not correct: " + text);
        }

        return categories[Integer.parseInt(text) - 1];
    }

    public boolean getBoolean(String message) {
        System.out.println(message + "[y / n]?");
        return Character.toLowerCase(scanner.nextLine().charAt(0)) == 'y';
    }

    public BigDecimal getBigDecimal(String message){
        System.out.println(message);
        String text = scanner.nextLine();

        if(!text.matches("[0-9]+.*[0-9]+]")){
            throw new AppException("BIGDECIMAL VALUE IS NOT CORRECT: " + text);
        }

        return new BigDecimal(text);
    }

    public String getStringOfLetters(String message, boolean upperCase){
        System.out.println(message);

        String text = scanner.nextLine();

        if(upperCase && !text.matches("[A-Z]+")){
            throw new AppException("STRING IS NOT CORRECT: " + text);
        }
        else if(!upperCase && !text.matches("[a-z]+")){
            throw new AppException("STRING IS NOT CORRECT: " + text);
        }

        return text;
    }

    public LocalDate getDate(String message){
        System.out.println(message);
        String text = scanner.nextLine();

        LocalDate date;
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            date = LocalDate.parse(text, formatter);
        }catch (Exception e){
            throw new AppException("DATE IS NOT CORRECT: " + text);
        }

        return date;
    }

    public void close() {

        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
    }
}
