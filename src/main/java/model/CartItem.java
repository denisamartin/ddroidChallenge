package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class that defines a product that is in the shopping cart. This inherits the Product class.
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem extends Product{
    private Integer quantity;
    public CartItem(Product product, Integer quantity){
        super(product.getName(),product.getPrice(),product.getShippingCountry(),product.getWeight());
        this.quantity=quantity;
    }
}
