package use_case.search.by_language;

import java.util.*;
import java.util.stream.Collectors;

import entity.Country;

/**
 * Use Case Interactor for the Search by Language use case.
 */
public class SearchByLanguageInteractor implements SearchByLanguageInputBoundary {
    private final SearchByLanguageCountryDataAccessInterface countryDataAccess;
    private final SearchByLanguageOutputBoundary searchByLanguagePresenter;

    public SearchByLanguageInteractor(SearchByLanguageCountryDataAccessInterface countryDataAccess, SearchByLanguageOutputBoundary searchByLanguagePresenter) {
        this.countryDataAccess = countryDataAccess;
        this.searchByLanguagePresenter = searchByLanguagePresenter;
    }

    /**
     * Prepares language options for the user to select.
     */
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

    /**
     * Executes the main logic of the use case, including main flow and alternate flows.
     * @param inputData the input data
     */
    @Override
    public void execute(SearchByLanguageInputData inputData) {
        String language = inputData.getLanguage();
        List<Country> allCountries = countryDataAccess.getAllCountries();

        // Alternative flow: invalid (not present) language input
        if (language == null || language.isEmpty()) {
            searchByLanguagePresenter.prepareFailView("Please select a language before searching.");
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
            // Alternative flow: No countries found by language filter
            searchByLanguagePresenter.prepareFailView("No countries found for language: " + language);
        } else {
            // Main flow
            SearchByLanguageOutputData outputData = new SearchByLanguageOutputData(null, language, countries);
            searchByLanguagePresenter.presentCountries(outputData);
        }
    }

    @Override
    public void switchToPreviousView() {
        searchByLanguagePresenter.switchToPreviousView();
    }

    @Override
    public void switchToSaveCountryView() {
        searchByLanguagePresenter.switchToSaveCountryView();
    }

}
