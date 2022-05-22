package controller;

import model.ShoppingService;

import java.util.Scanner;

/**
 * The controller class deals with the given commands
 */
public class Controller {
    private final Scanner scanner = new Scanner(System.in);
    private final ShoppingService service = new ShoppingService();

    public void run() throws Exception {
        print("Welcome to my awesome Shopping cart.");
        boolean done = false;
        while (!done) {
            print("Enter a command: ");
            String command = scanner.nextLine();
            try {
                done = handleCommand(command);
            } catch (Exception e) {

            }
        }
    }

    private boolean handleCommand(String command) {
        switch (command) {
            case "catalog":
                handleListProducts();
                return false;
            case "create order":
                handleCreateOrder();
                return false;
            case "add":
                handleAddToOrder();
                return false;
            case "checkout":
                handleCheckout();
                return false;
            case "exit":
                return true;
            default:
                print("Unknown command. Try again.");
                return false;
        }
    }

    public void handleListProducts() {
        print(service.toString());
    }

    public void handleCreateOrder() {
        service.createNewOrder();
        print("New order with id " + service.getOrderId() + " has been created");
    }

    public void handleAddToOrder() {
        print("Product name: ");
        String name = scanner.nextLine();
        service.addToOrder(name);
        service.printCurrentOrder();
    }

    public void handleCheckout() {
        service.computeTotals();
        service.checkForDiscounts();
        service.applyDiscount();
        service.printInvoice();
    }


    private void print(String value) {
        System.out.println(value);
    }
}
