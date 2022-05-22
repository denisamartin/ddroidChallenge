package model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Here we deal with the actions on the product basket and orders
 */
@Getter
@Setter
public class ShoppingService implements IShoppingServiceProcessing {

    private static final LinkedHashMap<Product, Double> productsWithShipping = new LinkedHashMap<>();
    private static final HashMap<Order, ArrayList<CartItem>> orders = new HashMap<>();
    private int orderId = 0;

    /**
     * Calculate the shipping fee for each product
     *
     * @pre product!=null
     */
    @Override
    public Double calculateShipping(Product product) {
        assert product != null;
        Integer shippingPrice = ShippingRate.getShippingPrices().get(product.getShippingCountry());
        BigDecimal bd = new BigDecimal((product.getWeight() / 0.1) * shippingPrice).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Add a new product to the  productsWithShipping
     *
     * @param product the product we want to add
     * @pre product != null;
     * @post productsWithShipping contains the created product
     * @invariant productsWithShipping!=null
     */
    @Override
    public void addProduct(Product product) {
        assert product != null;
        productsWithShipping.put(product, calculateShipping(product));
        assert productsWithShipping.containsKey(product);
        assert isWellFormed();
    }

    /**
     * Create a new order
     *
     * @post list of ordered products! = null
     * @invariant productsWithShipping!=null
     */
    @Override
    public void createNewOrder() {
        orderId++;
        Order o = new Order(orderId, 0.0, 0, 0.0, 0.0);
        ArrayList<CartItem> lista = new ArrayList<>();
        orders.put(o, lista);
        assert lista != null;
        assert isWellFormed();
    }

    /**
     * Add the desired product to the order
     *
     * @param product :product name
     * @pre product!=null;
     * @post desired product! = null and the product was successfully added to the order
     * @invariant productsWithShipping!=null
     */
    @Override
    public void addToOrder(String product) {
        assert product != null;
        Product item = getMyProduct(product);
        if (item != null) {
            boolean inMap = false;
            Map.Entry<Order, ArrayList<CartItem>> entry = getCurrentOrder();
            for (CartItem item1 : entry.getValue()) {
                if (item1.getName().equals(item.getName())) {
                    item1.setQuantity(item1.getQuantity() + 1);
                    inMap = true;
                    break;
                }
            }
            if (!inMap) {
                entry.getValue().add(new CartItem(item, 1));
            }

            assert entry.getValue().contains(new CartItem(item, 1));
        }
        assert isWellFormed();
    }

    /**
     * Calculate the total, subtotal, VAT and shipping fee
     *
     * @invariant productsWithShipping!=null
     */
    @Override
    public void computeTotals() {
        double subtotal = 0.0;
        int shipping = 0;
        Map.Entry<Order, ArrayList<CartItem>> entry = getCurrentOrder();
        for (CartItem item : entry.getValue()) {
            subtotal += item.getQuantity() * item.getPrice();
            shipping += item.getQuantity() * productsWithShipping.get(getMyProduct(item.getName()));
        }
        double vat = 0.19 * subtotal;
        entry.getKey().setShipping(shipping);
        entry.getKey().setSubtotal(new BigDecimal(subtotal).setScale(2, RoundingMode.HALF_UP).doubleValue());
        entry.getKey().setVat(new BigDecimal(vat).setScale(2, RoundingMode.HALF_UP).doubleValue());
        entry.getKey().calculateTotal();
    }

    /**
     * Check for discounts that may apply
     *
     * @invariant productsWithShipping!=null
     */
    @Override
    public void checkForDiscounts() {
        Map.Entry<Order, ArrayList<CartItem>> entry = getCurrentOrder();
        entry.getKey().setDiscounts(new ArrayList<>());
        checkForTwoItemsDiscounts();
        checkForKeyboardDiscount();
        checkForTwoMonitorsDiscount();
    }

    /**
     * Apply the discounts found
     */
    @Override
    public void applyDiscount() {
        Map.Entry<Order, ArrayList<CartItem>> entry = getCurrentOrder();
        if (!entry.getKey().getDiscounts().isEmpty()) {
            for (Discount discount : entry.getKey().getDiscounts()) {
                entry.getKey().setTotal(new BigDecimal(entry.getKey().getTotal() - discount.getDiscountValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
        }
    }

    public void printInvoice() {
        Map.Entry<Order, ArrayList<CartItem>> entry = getCurrentOrder();
        System.out.println("\nInvoice\n");
        System.out.println("Subtotal: $" + entry.getKey().getSubtotal());
        System.out.println("Shipping: $" + entry.getKey().getShipping());
        System.out.println("VAT: $" + entry.getKey().getVat());
        if (entry.getKey().isDiscount()) {
            System.out.println("\nDiscounts");
            for (Discount discount : entry.getKey().getDiscounts()) {
                System.out.println(discount);
            }
        }
        System.out.println("\nTotal: $" + entry.getKey().getTotal());

    }

    public Map.Entry<Order, ArrayList<CartItem>> getCurrentOrder() {
        for (HashMap.Entry<Order, ArrayList<CartItem>> entry : orders.entrySet()) {
            if (entry.getKey().getOrderId() == orderId) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Returns a product by name
     * @pre product!=null
     * @param product product name
     * @return the desired product
     */
    public Product getMyProduct(String product) {
        assert product != null;
        for (Product p : productsWithShipping.keySet()) {
            if (p.getName().toLowerCase().contains(product.toLowerCase())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Print the current order
     */
    public void printCurrentOrder() {
        Map.Entry<Order, ArrayList<CartItem>> entry = getCurrentOrder();
        for (CartItem item : entry.getValue()) {
            System.out.println(item.getName() + " x " + item.getQuantity());
        }
    }

    /**
     * We check if there is a discount in which we have more than two products
     */
    public void checkForTwoItemsDiscounts() {
        Map.Entry<Order, ArrayList<CartItem>> entry = getCurrentOrder();
        if (entry.getValue().size() >= 2) {
            Discount discount = new Discount();
            discount.setDiscountType(DiscountType.TWO_ITEMS_DISCOUNT);
            if (entry.getKey().getShipping() >= 10) {
                discount.setDiscountValue(10);
            } else {
                discount.setDiscountValue(entry.getKey().getShipping());
            }
            entry.getKey().getDiscounts().add(discount);
            entry.getKey().setDiscount(true);
        }
    }

    /**
     * We check if we have the discount for the keyboard
     */
    public void checkForKeyboardDiscount() {
        Map.Entry<Order, ArrayList<CartItem>> entry = getCurrentOrder();
        for (CartItem cartItem : entry.getValue()) {
            if (cartItem.getName().equals("Keyboard")) {
                Discount discount = new Discount();
                discount.setDiscountType(DiscountType.KEYBOARD_DISCOUNT);
                double value = cartItem.getQuantity() * cartItem.getPrice() * 0.1;
                discount.setDiscountValue(new BigDecimal(value).setScale(2, RoundingMode.FLOOR).doubleValue());
                entry.getKey().getDiscounts().add(discount);
                entry.getKey().setDiscount(true);
            }
        }
    }

    /**
     * We check if we have the discount for two monitors
     */
    public void checkForTwoMonitorsDiscount() {
        Map.Entry<Order, ArrayList<CartItem>> entry = getCurrentOrder();
        boolean isMonitor = false;
        boolean isLamp = false;
        double priceLamp = 0.0;
        for (CartItem cartItem : entry.getValue()) {
            if (cartItem.getName().equals("Monitor")) {
                if (cartItem.getQuantity() >= 2) {
                    isMonitor = true;
                }
            }
            if (cartItem.getName().equals("Desk Lamp")) {
                isLamp = true;
                priceLamp = cartItem.getPrice();
            }
        }
        if (isMonitor && isLamp) {
            Discount discount = new Discount();
            discount.setDiscountType(DiscountType.TWO_MONITORS_DISCOUNT);
            discount.setDiscountValue(priceLamp / 2);
            entry.getKey().getDiscounts().add(discount);
            entry.getKey().setDiscount(true);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Product, Double> p : productsWithShipping.entrySet()) {
            stringBuilder.append(p.getKey().getName());
            stringBuilder.append(" - ");
            stringBuilder.append("$").append(p.getKey().getPrice());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Invariant
     *
     * @return true if productsWithShipping! = null and false otherwise
     */
    public boolean isWellFormed() {
        return productsWithShipping != null;
    }

}
