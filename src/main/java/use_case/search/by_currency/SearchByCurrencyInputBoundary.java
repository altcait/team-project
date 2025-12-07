package use_case.search.by_currency;


/**
 * Input boundary for the Search by currency use case.
 * The interactor of Search by currency use case implements this interface.
 */
public interface SearchByCurrencyInputBoundary {

    /** Return all the countries under the currency based on the currency.
     */
    void searchCountriesByCurrency(SearchByCurrencyInputData inputData);

    /** Return the list of Currencies (after deduplication)
     */
    void listCurrencies();


    /** Switch to Save Country View
     */
    void switchToSaveCountryView();

    /** Switch to Profile View
     */
    void switchToSelectedListView();

}
