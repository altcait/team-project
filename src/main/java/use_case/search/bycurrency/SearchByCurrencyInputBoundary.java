package use_case.search.bycurrency;

/**
 * Input boundary for the Search by Currency use case.
 * The interactor of the currency search use case implements this interface.
 */
public interface SearchByCurrencyInputBoundary {

    /** Return all countries that use the given currency. */
    void searchCountriesByCurrency(SearchByCurrencyInputData inputData);

    /** Return the list of available currencies (after deduplication). */
    void listCurrencies();

    /** Switch to Save Country View */
    void switchToSaveCountryView();

    /** Switch to Profile View */
    void switchToProfileView();
}
