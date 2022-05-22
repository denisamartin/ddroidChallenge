package model;


import java.util.HashMap;

/**
 * The class that contains shipping rates
 */

public class ShippingRate {
    private static final HashMap<Country, Integer> shippingPrices = new HashMap<>() {
        {
            put(Country.RO, 1);
            put(Country.UK, 2);
            put(Country.US, 3);
        }
    };

    public static HashMap<Country, Integer> getShippingPrices() {
        return shippingPrices;
    }
}
