package interface_adapter.search.bycurrency;

import use_case.search.bycurrency.SearchByCurrencyInputBoundary;
import use_case.search.bycurrency.SearchByCurrencyInputData;

/**
 * Controller for the Search by Currency use case.
 * Called by the view in response to user actions.
 */
public class SearchByCurrencyController {

    private final SearchByCurrencyInputBoundary interactor;

    public SearchByCurrencyController(SearchByCurrencyInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadCurrencies() {
        // Called when we need to load all regions (e.g., when the view is first shown)
        interactor.listCurrencies();
    }

    /**
     * Called when the user enters a currency code or name and triggers a search.
     */
    public void onSearch(String currency) {
        if (currency == null || currency.isEmpty()) {
            // Optional: handle empty input
            interactor.searchCountriesByCurrency(new SearchByCurrencyInputData(null));
        } else {
            interactor.searchCountriesByCurrency(new SearchByCurrencyInputData(currency));
        }
    }

    /**
     * Called when the user clicks the add country button
     */
    public void onAddCountryButtonClicked() {
        interactor.switchToSaveCountryView();
    }

    /**
     * Called when the user wants to go back to their profile
     */
    public void onBackToProfileButtonClicked() {
        interactor.switchToProfileView();
    }
}
