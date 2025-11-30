package use_case.search.bycurrency;

/**
 * Input data for the Search by Currency use case.
 * Wraps the currency selected by the user.
 */
public class SearchByCurrencyInputData {

    private final String currency;

    public SearchByCurrencyInputData(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
