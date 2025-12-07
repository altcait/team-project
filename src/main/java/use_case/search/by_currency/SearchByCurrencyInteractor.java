package use_case.search.by_currency;

import entity.Country;

import java.util.*;

/**
 * Interactor for the Search by Currency use case.
 */
public class SearchByCurrencyInteractor implements SearchByCurrencyInputBoundary {

    private final SearchByCurrencyDataAccessInterface dataAccess;
    private final SearchByCurrencyOutputBoundary presenter;

    public SearchByCurrencyInteractor(SearchByCurrencyDataAccessInterface dataAccess,
                                    SearchByCurrencyOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    /**
     * Searching countries by Currency: return all the countries under the Currency.
     */
    @Override
    public void searchCountriesByCurrency(SearchByCurrencyInputData inputData) {
        System.out.println("[Interactor] Searching for currency: " + inputData.getCurrency());


        String currency = inputData.getCurrency();
        if (currency == null || currency.isBlank()) {
            presenter.prepareFailView("Currency must not be empty.");
            return;
        }

        List<Country> allCountries = dataAccess.getAllCountries();
        List<Country> countries = new ArrayList<>();
        System.out.println("[Interactor] All countries count: " + allCountries.size());
        for (Country country : allCountries) {
            List<String> countryCurrencies = country.getCurrencies();
            if (countryCurrencies != null && countryCurrencies.contains(currency)) {
                countries.add(country);
            }
        }

        if (countries.isEmpty()) {
            presenter.prepareFailView("No countries found for Currency: " + currency);
        } else {
            SearchByCurrencyOutputData outputData =
                    new SearchByCurrencyOutputData(currency, countries,  null);
            presenter.presentCountries(outputData);
        }
    }

    /**
     * Listing all Currencies: return all existing Currencies.
     */
    @Override
    public void listCurrencies() {
        List<Country> allCountries = dataAccess.getAllCountries();
        Set<String> currenciesSet = new HashSet<>();

        for (Country country : allCountries) {
            List<String> currencies = country.getCurrencies();
            if (currencies != null) {
                currenciesSet.addAll(currencies);
            }
        }

        if (currenciesSet.isEmpty()) {
            presenter.prepareFailView("No Currencies found.");
        } else {
            List<String> currencies = new ArrayList<>(currenciesSet);
            Collections.sort(currencies);

            SearchByCurrencyOutputData outputData =
                    new SearchByCurrencyOutputData(null,  null, currencies);
            presenter.presentCurrencies(outputData);
        }
    }

    /**
     * Listing when click the add button.
     */
    @Override
    public void switchToSaveCountryView() {
        presenter.switchToSaveCountryView();
    }

    /**
     * Listing when click the back to the selected list button.
     */
    @Override
    public void switchToSelectedListView() {
        presenter.switchToSelectedListView();
    }
}