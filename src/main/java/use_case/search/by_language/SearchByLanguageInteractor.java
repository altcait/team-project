package use_case.search.by_language;

import java.util.*;
import java.util.stream.Collectors;

import entity.Country;    // TODO: get updated Country entity from remote repo

/**
 * Use Case Interactor for the Search by Language use case.
 * Main flow: TODO
 * 1. searching countries by language
 * 2.
 */
public class SearchByLanguageInteractor implements SearchByLanguageInputBoundary {
    private final SearchByLanguageCountryDataAccessInterface countryDataAccess;
    private final SearchByLanguageOutputBoundary searchByLanguagePresenter;

    public SearchByLanguageInteractor(SearchByLanguageCountryDataAccessInterface countryDataAccess, SearchByLanguageOutputBoundary searchByLanguagePresenter) {
        this.countryDataAccess = countryDataAccess;
        this.searchByLanguagePresenter = searchByLanguagePresenter;
    }

    @Override
    public void languageOptions() {
        List<Country> allCountries = countryDataAccess.getAllCountries();

        // Create a set of all unique languages amongst the countries in alphabetical order
        Set<String> languagesSet = allCountries
                .stream()
                .flatMap(country -> country.getLanguage().stream())
                .collect(Collectors.toCollection(TreeSet::new));

        if (languagesSet.isEmpty()) {
            searchByLanguagePresenter.prepareFailView("No languages found.");
        } else {
            SearchByLanguageOutputData outputData =
                    new SearchByLanguageOutputData(languagesSet, null, null);
            searchByLanguagePresenter.presentLanguages(outputData);
        }
    }

    @Override
    public void execute(SearchByLanguageInputData inputData) {
        String language = inputData.getLanguage();
        List<Country> allCountries = countryDataAccess.getAllCountries(); // TODO: get updated Country entity from remote repo

        // Check for a valid (present) language input
        if (language == null || language.isEmpty()) {
            searchByLanguagePresenter.prepareFailView("No language selected.");
            return;
        }

        List<Country> countries = new ArrayList<>();    // list of countries filtered by user-inputted language

        for (Country country : allCountries) {
            List<String> countryLanguage = country.getLanguage();
            if (countryLanguage != null && countryLanguage.contains(language)) {
                countries.add(country);
            }
        }

        if (countries.isEmpty()) {
            // No countries found by language filter
            searchByLanguagePresenter.prepareFailView("No countries found for language: " + language);
        } else {
            SearchByLanguageOutputData outputData = new SearchByLanguageOutputData(null, language, countries);
            searchByLanguagePresenter.presentCountries(outputData);
        }
    }

    // TODO: "back" from Search view(s)
    @Override
    public void switchToPreviousView() {
        searchByLanguagePresenter.switchToPreviousView();
    }

    public void switchToSaveCountryView() {
        searchByLanguagePresenter.switchToSaveCountryView();
    }

}
