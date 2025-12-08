package interface_adapter.search.by_language;

import entity.Country;

import java.util.List;
import java.util.Set;

/**
 * UI state object for the Search by Language view model.
 */
public class SearchByLanguageState {

    private Set<String> languageOptions;
    private String selectedLanguage;
    private List<Country> countries;
    private String errorMessage;

    // Getters

    public Set<String> getLanguageOptions() {
        return languageOptions;
    }

    public String getSelectedLanguage() {
        // this is used by Controller to get the language selected by user
        return selectedLanguage;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // Setters

    public void setLanguageOptions(Set<String> languageOptions) {
        this.languageOptions = languageOptions;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        // this is used by Controller to get the region selected by user
        this.selectedLanguage = selectedLanguage;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}