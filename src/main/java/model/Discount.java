package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class that defines a discount
 */
@Getter
@Setter
@NoArgsConstructor
public class Discount {
    private double discountValue;
    private DiscountType discountType;
    private Integer quantity;

    @Override
    public String toString() {
        String type="";
        switch (discountType){
            case KEYBOARD_DISCOUNT:
                type= "10% off keyboards: ";
                break;
            case  TWO_ITEMS_DISCOUNT:
                type= "$"+ (int)discountValue + " off shipping: ";
                break;
            case TWO_MONITORS_DISCOUNT:
                type= "$"+ discountValue + " off lamp: ";
                break;
        }
        return type + "-$" + discountValue ;
    }
}
