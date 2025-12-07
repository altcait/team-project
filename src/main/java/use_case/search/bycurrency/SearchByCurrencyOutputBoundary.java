package use_case.search.bycurrency;


/**
 * Output boundary for the Search by Currency use case.
 * The presenter of Search by Currency use case implements this interface.
 */
public interface SearchByCurrencyOutputBoundary {

    /**
     * Called when we have a list of countries to show
     * (for search-by-Currency).
     */
    void presentCountries(SearchByCurrencyOutputData outputData);



    /**
     * Called when we have a list of all Currencies.
     */
    void presentCurrencies(SearchByCurrencyOutputData outputData);

    /**
     * Called when something goes wrong (invalid input, no results, etc.).
     */
    void prepareFailView(String errorMessage);

    /**
     * Called when the user want to add selected country to the user's list.
     */
    void switchToSaveCountryView();

    /**
     * Called when the user want to back to user's profile.
     */
    void switchToSelectedListView();
}
