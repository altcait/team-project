package use_case.search.by_language;

import java.util.List;
import java.util.Set;

import entity.Country;    // TODO: get updated Country entity from remote repo

/**
 * Output data for the Search by Language use case.
 */
public class SearchByLanguageOutputData {
    private final Set<String> languageOptions;
    private final String selectedLanguage;
    private final List<Country> countries;    // TODO: get updated Country entity from remote repo

    public SearchByLanguageOutputData(Set<String> languageOptions, String selectedLanguage, List<Country> countries) {
        this.languageOptions = languageOptions;
        this.selectedLanguage = selectedLanguage;
        this.countries = countries;
    }

    public Set<String> getLanguageOptions() {
        return languageOptions;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public List<Country> getCountries() {    // TODO: get updated Country entity from remote repo
        return countries;
    }
}
