package use_case.search.ByLanguage;

import java.util.*;

import entity.Country;    // TODO: get updated Country entity from remote repo

/**
 * Use Case Interactor for the Search by Language use case.
 * Main flow: TODO
 * 1. searching countries by language
 * 2. listing all regions
 */
public class SearchByLanguageInteractor implements SearchByLanguageInputBoundary {
    private final SearchByLanguageCountryDataAccessInterface countryDataAccess;
    private final SearchByLanguageOutputBoundary searchByLanguagePresenter;

    public SearchByLanguageInteractor(SearchByLanguageCountryDataAccessInterface countryDataAccess, SearchByLanguageOutputBoundary searchByLanguagePresenter) {
        this.countryDataAccess = countryDataAccess;
        this.searchByLanguagePresenter = searchByLanguagePresenter;
    }

    // TODO: does this have additional info that needs its own javadoc?
    @Override
    public void languageOptions() {
        List<Country> allCountries = countryDataAccess.getAllCountries();
        Set<List<String>> languagesSet = new HashSet<>();

        for (Country country : allCountries) {
            List<String> language = country.getLanguage();
            if (language != null) {
                languagesSet.add(language);
            }
        }

        if (languagesSet.isEmpty()) {
            searchByLanguagePresenter.prepareFailView("No languages found.");
        } else {
            List<List<String>> languages = new ArrayList<>(languagesSet);
            //Collections.sort(languages); // TOOD: pretty sure sorting languages is not necessary

            SearchByLanguageOutputData outputData =
                    new SearchByLanguageOutputData(languagesSet, null, null);
            searchByLanguagePresenter.presentLanguages(outputData);
        }
    }

    // TODO: does this have additional info that needs its own javadoc?
    @Override
    public void execute(SearchByLanguageInputData inputData) {
        String language = inputData.getLanguage();  // TODO: should be final?
        List<Country> allCountries = countryDataAccess.getAllCountries(); // TODO: get updated Country entity from remote repo

        // Check for a valid (present) language input
        if (language == null) {
            searchByLanguagePresenter.prepareFailView("Language must not be empty.");
            return;
        }

        List<Country> Countries = new ArrayList<>();    // list of countries filtered by inputted language

        for (Country country : allCountries) {
            List<String> countryLanguage = country.getLanguage();
            if (countryLanguage != null & countryLanguage.contains(language)) {
                Countries.add(country);
            }
        }

        // No countries found by language filter
        if (Countries.isEmpty()) {
            searchByLanguagePresenter.prepareFailView("No countries found for language: " + language);
        } else {
            SearchByLanguageOutputData outputData = new SearchByLanguageOutputData(null, language, Countries);
            searchByLanguagePresenter.presentCountries(outputData);
        }
    }

    // TODO: "back" from Search view(s)
    @Override
    public void switchToPreviousView() {
        searchByLanguagePresenter.switchToPreviousView();
    }

    // TODO
}
