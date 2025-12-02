package interface_adapter.search.bycurrency;

import use_case.search.bycurrency.*;


/**
 * Controller for the Search by Region use case.
 * Called by the view in response to user actions.
 */
public class SearchByCurrencyController {

    private final SearchByCurrencyInputBoundary interactor;

    public SearchByCurrencyController(SearchByCurrencyInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadCurrencies() {
        interactor.listCurrencies();
    }

//    public void onCurrencySelected(String currency) {
//        SearchByCurrencyInputData inputData = new SearchByCurrencyInputData(currency);
//    }
    public void onCurrencySelected(String currency) {
        if (currency != null) {
            interactor.searchCountriesByCurrency(new SearchByCurrencyInputData(currency));
        }
    }


    public void onSearch(String currency) {
        System.out.println("onSearch clicked! Currency: " + currency);
        if (currency == null) {
            interactor.searchCountriesByCurrency(
                    new SearchByCurrencyInputData(null));
            return;
        }

        SearchByCurrencyInputData inputData =
                new SearchByCurrencyInputData(currency);
        interactor.searchCountriesByCurrency(inputData);

    }

    public void onAddCountryButtonClicked() {
        interactor.switchToSaveCountryView();
    }

    public void onBackToSelectedListButtonClicked() {
        interactor.switchToSelectedListView();
    }

}
