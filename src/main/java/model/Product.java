package model;
import lombok.*;

import java.util.Objects;

/**
 * The class that defines a product.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    private String name;
    private Double price;
    private Country shippingCountry;
    private Double weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name) && Objects.equals(price, product.price) && shippingCountry == product.shippingCountry && Objects.equals(weight, product.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, shippingCountry, weight);
    }
}
