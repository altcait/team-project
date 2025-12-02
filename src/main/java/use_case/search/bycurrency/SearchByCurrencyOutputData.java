package use_case.search.bycurrency;

import entity.Country;

import java.util.List;

/**
 * Output data for the Search by Currency use case.
 */

public class SearchByCurrencyOutputData {

    private final String currency;
    private final List<Country> countries;
    private final List<String> currencies;

    public SearchByCurrencyOutputData(String currency,
                                    List<Country> countries,
                                    List<String> currencies) {
        this.currency = currency;
        this.countries = countries;
        this.currencies = currencies;
    }

    public String getCurrency() {
        return currency;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public List<String> getCurrencies() {
        return currencies;
    }
}