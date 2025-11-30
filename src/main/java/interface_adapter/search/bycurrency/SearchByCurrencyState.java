package interface_adapter.search.bycurrency;

import entity.Country;
import java.util.List;

/**
 * State object for the Search by Currency view.
 * Holds all data that the UI needs to display.
 */
public class SearchByCurrencyState {

    private String selectedCurrency;
    private List<String> currencyOptions;
    private List<Country> countries;
    private String errorMessage;

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public List<String> getCurrencyOptions() {
        return currencyOptions;
    }

    public void setCurrencyOptions(List<String> currencyOptions) {
        this.currencyOptions = currencyOptions;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
