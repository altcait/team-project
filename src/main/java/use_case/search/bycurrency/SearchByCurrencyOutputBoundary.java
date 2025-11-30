package use_case.search.bycurrency;

import entity.Country;
import java.util.List;

/**
 * Output boundary for the Search by Currency use case.
 * The presenter of Search by Currency implements this interface.
 */
public interface SearchByCurrencyOutputBoundary {

    /**
     * Called when we have a list of countries that use the given currency.
     */
    void presentCountries(SearchByCurrencyOutputData outputData);

    /**
     * Called when we have a list of all available currencies.
     */
    void presentCurrencies(List<String> currencies);

    /**
     * Called when something goes wrong (invalid input, no results, etc.).
     */
    void prepareFailView(String errorMessage);

    /**
     * Called when the user wants to add a selected country to the user's list.
     */
    void switchToSaveCountryView();

    /**
     * Called when the user wants to go back to the user's profile.
     */
    void switchToProfileView();


}
