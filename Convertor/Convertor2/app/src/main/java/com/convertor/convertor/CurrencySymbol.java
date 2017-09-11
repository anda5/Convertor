package com.convertor.convertor;

import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Acer on 9/11/2017.
 */

public class CurrencySymbol {


    /**
     * Get the currencies code from the available locales information.
     *
     * @return a map of currencies code.
     */
    public Map getAvailableCurrencies() {
        Locale[] locales = Locale.getAvailableLocales();

        //
        // We use TreeMap so that the order of the data in the map sorted
        // based on the country name.
        //
        Map currencies = new TreeMap();
        for (Locale locale : locales) {
            try {
                currencies.put(locale.getDisplayCountry(),
                        Currency.getInstance(locale).getCurrencyCode());
            } catch (Exception e) {
                // when the locale is not supported
            }
        }
        return currencies;
    }
}