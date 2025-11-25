package interface_adapter.search.ByLanguage;

import entity.Country;

import java.util.List;

/**
 * UI state object for the Search by Language view model.
 * Holds all data that the UI needs to display with.    TODO: ?
 */
public class SearchByLanguageState {

    private List<String> languageOptions;
    private String selectedLanguage;
    private List<Country> countries;
    private String errorMessage;

    // Getters

    public List<String> getLanguageOptions() {
        return languageOptions;
    }

    public String getSelectedLanguage() {
        // this is used by Controller to get the region selected by user
        return selectedLanguage;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // Setters

    public void setLanguageOptions(List<String> languageOptions) {
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