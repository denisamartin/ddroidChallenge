package model;


import org.junit.jupiter.api.Test;

class ShoppingServiceTest {

    private static ShoppingService createService() {
        ShoppingService shoppingService = new ShoppingService();
        Product product1 = new Product("Mouse", 10.99, Country.RO, 0.2);
        Product product2 = new Product("Keyboard", 40.99, Country.UK, 0.7);
        Product product3 = new Product("Monitor", 164.99, Country.US, 1.9);
        Product product4 = new Product("Webcam", 84.99, Country.RO, 0.2);
        Product product5 = new Product("Headphones", 59.99, Country.US, 0.6);
        Product product6 = new Product("Desk Lamp", 89.99, Country.UK, 1.3);
        shoppingService.addProduct(product1);
        shoppingService.addProduct(product2);
        shoppingService.addProduct(product3);
        shoppingService.addProduct(product4);
        shoppingService.addProduct(product5);
        shoppingService.addProduct(product6);
        return shoppingService;
    }

    @Test
    void computeTotalsWithDiscount() {
        ShoppingService shoppingService = createService();
        shoppingService.createNewOrder();
        shoppingService.addToOrder("Keyboard");
        shoppingService.addToOrder("Monitor");
        shoppingService.addToOrder("Monitor");
        shoppingService.computeTotals();
        shoppingService.checkForDiscounts();
        shoppingService.applyDiscount();
        assert shoppingService.getCurrentOrder().getKey().getSubtotal() == 370.97;
        assert shoppingService.getCurrentOrder().getKey().getShipping() == 128;
        assert shoppingService.getCurrentOrder().getKey().getVat() == 70.48;
        assert shoppingService.getCurrentOrder().getKey().getTotal() == 555.36;
    }

}