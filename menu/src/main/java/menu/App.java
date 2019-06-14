package menu;

/**
 * Hello world!
 *
 */
//TODO use EmailService in OrdersService

public class App 
{
    public static void main( String[] args ) {

        MenuService menuService = new MenuService("resources/Orders.json");
        menuService.mainMenu();

        // EmailService emailService = new EmailService();
        // emailService.sendAsHtml("mati19115@wp.pl", "KM Programs", "xxx");
    }
}
