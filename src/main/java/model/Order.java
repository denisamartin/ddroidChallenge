package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * The class that defines an order.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private Double subtotal;
    private Integer shipping;
    private Double total;
    private Double vat;
    private boolean discount = false;
    private ArrayList<Discount> discounts = new ArrayList<>();

    public Order(int orderId, Double subtotal, Integer shipping, Double total, Double VAT) {
        this.orderId = orderId;
        this.subtotal = subtotal;
        this.shipping = shipping;
        this.total = total;
        this.vat = VAT;
    }

    /**
     * Calculate the total order by summing the partial results.
     */
    public void calculateTotal() {
        this.total = new BigDecimal(subtotal + shipping + vat).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
