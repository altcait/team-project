package interface_adapter.search.bycurrency;

import entity.Country;

import java.util.List;

public class SearchByCurrencyState {

    private String selectedCurrency;
    private List<String> currencyOptions;
    private List<Country> countries;
    private String errorMessage;

    public String getSelectedCurrency() {
        // this is used by Controller to get the currency selected by user
        return selectedCurrency;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        // this is used by Controller to get the currency selected by user
        this.selectedCurrency = selectedCurrency;
    }

    public List<String> getCurrencyOptions() {
        return currencyOptions;
    }

    public void setCurrencyOptions(List<String> regionOptions) {
        this.currencyOptions = regionOptions;
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
