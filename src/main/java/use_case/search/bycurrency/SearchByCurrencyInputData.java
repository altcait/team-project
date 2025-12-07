package use_case.search.bycurrency;

/**
 * Input data for the Search by Currency use case.
 */
public class SearchByCurrencyInputData {

    private final String currency;

    public SearchByCurrencyInputData(String currency) {
        if (currency != null && currency.isBlank()) {
            currency = null;
        }
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

}
