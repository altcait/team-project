package use_case.search.bycurrency;

import entity.Country;
import java.util.List;

/**
 * Output data for the Search by Currency use case.
 */
public class SearchByCurrencyOutputData {

    private final String currencyCode; // selected currency
    private final List<Country> countries; // countries using this currency
    private final List<String> currencies; // list of all available currencies

    public SearchByCurrencyOutputData(String currencyCode, List<Country> countries, List<String> currencies) {
        this.currencyCode = currencyCode;
        this.countries = countries;
        this.currencies = currencies;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public List<String> getCurrencies() {
        return currencies;
    }
}
