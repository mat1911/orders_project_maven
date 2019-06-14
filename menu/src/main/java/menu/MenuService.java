package menu;

import exceptions.AppException;
import service.OrdersService;
import service.UserDataService;

import java.time.LocalDate;

public class MenuService {
    private OrdersService ordersService;
    private final UserDataService userDataService;

    public MenuService(String jsonFilename) {
        ordersService = new OrdersService(jsonFilename);
        userDataService = new UserDataService();
    }

    public void mainMenu() {
        while (true) {
            try {

                showMenu();
                int option = userDataService.getInt("Enter number of selected option: ");

                switch(option){

                    case 1 -> option1();

                    case 2 -> option2();

                    case 3 -> option3();

                    case 4 -> option4();

                    case 5 -> option5();

                    case 6 -> option6();

                    case 7 -> option7();

                    case 8 -> option8();

                    case 9 -> option9();

                    case 10 -> option10();

                    case 11 ->{
                        System.out.println("Have a nice day!");
                        return;
                    }
                }

            } catch (AppException e) {
                System.out.println("\n--------------------- EXCEPTION ----------------------");
                System.out.println(e.getMessage());
            }
        }
    }

    private void showMenu() {

        System.out.println("=================MENU=================");
        System.out.println("1 - all orders");
        System.out.println("2 - average price of product oredered in the time period");
        System.out.println("3 - products with highest prices for all categories");
        System.out.println("4 - date with highest sale and date with lowest sale");
        System.out.println("5 - client who paid the highest price");
        System.out.println("6 - total price for all orders");
        System.out.println("7 - customers who always bought x number of products");
        System.out.println("8 - category bought the most");
        System.out.println("9 - number of products sold in a given month");
        System.out.println("10 - the most frequently purchased products in a given month");
        System.out.println("11 - end of app");
    }

    private void option1(){
        System.out.println(ordersService);
    }

    private void option2(){
        LocalDate from = userDataService.getDate("Enter date from: ");
        LocalDate to = userDataService.getDate("Enter date to: ");

        System.out.println("Average price = " + ordersService.averagePriceBetween(from, to));
    }

    private void option3(){
       ordersService.productWithHighestPrice().forEach(((category, product) -> System.out.println(category + " " + product)));
    }

    private void option4(){
        LocalDate[] dates = ordersService.datesWithHighestAndLowestSales();

        System.out.println("Lowest sale: " + dates[0]);
        System.out.println("Highest sale: " + dates[1]);
    }

    private void option5(){
        System.out.println(ordersService.personWhoPaidTheHighestPrice());
    }

    private void option6(){
        System.out.println("Total income: " + ordersService.totalIncomeWithDiscounts());
    }

    private void option7(){
        int numberOfProduct = userDataService.getInt("Enter number of products: ");

        System.out.println("Number of clients: " + ordersService.numberOfCustomersWithQuantityOfProduct(numberOfProduct));
    }

    private void option8(){
        System.out.println("Category with highest sale: " + ordersService.categoryWithHighestSales());
    }

    private void option9(){
        ordersService.quantityOfSoldProductPerMonth().forEach((month, number) -> System.out.println(month + " " + number));
    }

    private void option10(){
        ordersService.bestSellingCategoryPerMonth().forEach((month, category) -> System.out.println(month + " " + category));
    }
}
