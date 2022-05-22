import controller.Controller;
import model.ShoppingService;
import model.Country;
import model.Product;

public class MainClass {
    public static void main(String[] args) {
        ShoppingService shoppingService=new ShoppingService();
        Product product1= new Product("Mouse", 10.99, Country.RO,0.2);
        Product product2= new Product("Keyboard", 40.99, Country.UK,0.7);
        Product product3= new Product("Monitor", 164.99, Country.US,1.9);
        Product product4= new Product("Webcam", 84.99, Country.RO,0.2);
        Product product5= new Product("Headphones", 59.99, Country.US,0.6);
        Product product6= new Product("Desk Lamp", 89.99, Country.UK,1.3);
        shoppingService.addProduct(product1);
        shoppingService.addProduct(product2);
        shoppingService.addProduct(product3);
        shoppingService.addProduct(product4);
        shoppingService.addProduct(product5);
        shoppingService.addProduct(product6);
        Controller controller=new Controller();
        try {
            controller.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
