package generators;

import model.Customer;
import model.Order;
import model.Product;
import model.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

public class DataGenerator {
    final static private String[] POSSIBLE_PRODUCT_NAMES = {"KARTA GRAFICZNA", "PC", "MIKROFON", "MONITOR", "GLOSNIKI", "RADIO", "PROCESOR", "KAMERA", "APARAT", "TV"};
    final static private String[] POSSIBLE_NAMES = {"AGATA", "FRANEK", "STASIO", "BEATA", "ANNA", "MARCIN", "RADOSLAW", "MALGORZATA", "KRZYSZTOF", "MATEUSZ", "DOROTA", "MACIEJ", "PAWEL"};
    final static private String[] POSSIBLE_SURNAMES = {"PAWLAK", "NOWAK", "KOWAL", "STACH", "GENIUSZ", "GOLABEK", "KOLOS", "LEM", "DOKTOR", "STEPIEN", "MIKTUS", "GRUSZKA", "PAPLA"};
    final static private String[] POSSIBLE_DOMAINS = {"@gmail.com", "@wp.pl", "@interia.pl", "@onet.pl", "@o2.pl"};

    public static Order generateOrder(){
        Customer customer = Customer.builder()
                .age(generateNumber(18, 68))
                .name(generateName())
                .surname(generateSurname())
                .email(generateEmailAdress())
                .build();

        Product product = Product.builder()
                .name(generateProductName())
                .category(generateCategory())
                .price(new BigDecimal(generateNumber(267, 3590)))
                .build();

        return Order.builder()
                .customer(customer)
                .product(product)
                .quantity(generateNumber(0, 3))
                .orderDate(generateOrderDate())
                .build();
    }

    public static LocalDate generateOrderDate(){
        return LocalDate.now().plusDays(generateNumber(1, 90));
    }

    public static String generateName(){
        Random draw = new Random();
        return POSSIBLE_NAMES[draw.nextInt(POSSIBLE_NAMES.length)];
    }

    public static String generateProductName(){
        Random draw = new Random();
        return POSSIBLE_PRODUCT_NAMES[draw.nextInt(POSSIBLE_PRODUCT_NAMES.length)];
    }

    public static String generateSurname(){
        Random draw = new Random();
        return POSSIBLE_SURNAMES[draw.nextInt(POSSIBLE_SURNAMES.length)];
    }

    public static int generateNumber(int from, int to){
        Random draw = new Random();
        if(from > to){
            throw new IllegalArgumentException("Argument from should be 'lower' than argument 'to'");
        }
        return draw.nextInt(to - from) + from + 1;
    }

    public static String generateEmailAdress(){
        StringBuilder leftSideOfTheEmail = new StringBuilder();
        Random draw = new Random();

        for (int i = 0; i < draw.nextInt(6) + 1; i++) {
            leftSideOfTheEmail.append((char)('a' + draw.nextInt(26)));
        }

        return leftSideOfTheEmail.toString() + POSSIBLE_DOMAINS[draw.nextInt(POSSIBLE_DOMAINS.length)];
    }

    public static Category generateCategory(){
        Random draw = new Random();
        Category[] arrayOfCat = Category.values();

        return arrayOfCat[draw.nextInt(arrayOfCat.length)];
    }

}
