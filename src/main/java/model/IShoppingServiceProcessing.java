package model;

public interface IShoppingServiceProcessing {

    Double calculateShipping(Product product);
    void addProduct(Product product);
    void createNewOrder();
    void addToOrder(String product);
    void computeTotals();
    void checkForDiscounts();
    void applyDiscount();

}
