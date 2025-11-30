package use_case.search.bycurrency;

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
     * Searching countries by currency: return all the countries using the given currency.
     */


    @Override
    public void searchCountriesByCurrency(SearchByCurrencyInputData inputData) {
        String currency = inputData.getCurrency();
        if (currency == null || currency.isEmpty()) {
            presenter.prepareFailView("Currency must not be empty.");
            return;
        }

        List<Country> allCountries = dataAccess.getAllCountries();
        List<Country> matchedCountries = new ArrayList<>();

        for (Country country : allCountries) {
            List<String> countryCurrencies = country.getCurrencies();
            if (countryCurrencies != null) {
                for (String c : countryCurrencies) {
                    if (c != null && c.equalsIgnoreCase(currency)) {
                        matchedCountries.add(country);
                        break; // Stop checking other currencies for this country
                    }
                }
            }
        }

        if (matchedCountries.isEmpty()) {
            presenter.prepareFailView("No countries found using currency: " + currency);
        } else {
            SearchByCurrencyOutputData outputData =
                    new SearchByCurrencyOutputData(currency, matchedCountries, null);
            presenter.presentCountries(outputData);
        }
    }

    /**
     * Listing all currencies across countries (deduplicated and sorted).
     */
    @Override
    public void listCurrencies() {
        List<Country> allCountries = dataAccess.getAllCountries();
        Set<String> currencySet = new HashSet<>();

        for (Country country : allCountries) {
            List<String> countryCurrencies = country.getCurrencies();
            if (countryCurrencies != null) {
                for (String c : countryCurrencies) {
                    if (c != null && !c.isEmpty()) {
                        currencySet.add(c.toUpperCase());
                    }
                }
            }
        }

        if (currencySet.isEmpty()) {
            presenter.prepareFailView("No currencies found.");
        } else {
            List<String> currencies = new ArrayList<>(currencySet);
            Collections.sort(currencies);
            presenter.presentCurrencies(currencies);
        }
    }

    /**
     * Switch to Save Country view.
     */
    @Override
    public void switchToSaveCountryView() {
        presenter.switchToSaveCountryView();
    }

    /**
     * Switch back to Profile view.
     */
    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }
}
