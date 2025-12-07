package use_case.search.by_language;

/**
 * Input boundary for the Search by Language use case implemented by SearchByLanguageInteractor.
 */
public interface SearchByLanguageInputBoundary {

    /**
     * Executes all necessary operations to load all languages for when the first screen of the view is shown.
     */
    void languageOptions();

    // TODO: "back" from Search view(s)
    /**
     * Executes the "return to previous view" use case(?).
     */
    void switchToPreviousView();

    /**
     * Executes the Search by Language use case.
     * @param inputData the input data
     */
    void execute(SearchByLanguageInputData inputData);

    void switchToSaveCountryView();
}
