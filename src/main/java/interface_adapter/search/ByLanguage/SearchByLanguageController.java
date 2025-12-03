package interface_adapter.search.ByLanguage;

import use_case.search.ByLanguage.SearchByLanguageInputBoundary;
import use_case.search.ByLanguage.SearchByLanguageInputData;

/**
 * Controller for the Search by Language use case.
 */
public class SearchByLanguageController {

    private final SearchByLanguageInputBoundary searchByLanguageInteractor;

    public SearchByLanguageController(SearchByLanguageInputBoundary interactor) {
        this.searchByLanguageInteractor = interactor;
    }

    /**
     * Loads all language options the user can query with for the initial screen of the view (before any queries).
     */
    public void loadLanguages() {
        searchByLanguageInteractor.languageOptions();
    }

    /**
     * Instructs the Search by Language use case interactor to execute a search when user selects a language.
     * @param language the language to query with
     */
    public void execute(String language) {
        SearchByLanguageInputData inputData = new SearchByLanguageInputData(language);
        searchByLanguageInteractor.execute(inputData);
    }

    /**
     * Executes the "return to previous view" use case(?).
     */
    public void switchToPreviousView() {
        searchByLanguageInteractor.switchToPreviousView();
    }

    public void switchToSaveCountryView() {
        searchByLanguageInteractor.switchToSaveCountryView();
    }
}