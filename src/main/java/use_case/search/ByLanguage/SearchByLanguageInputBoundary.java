package use_case.search.ByLanguage;

/**
 * Input boundary for the Search by Language use case implemented by SearchByLanguageInteractor.
 *   It supports TODO operations:
 *   1, TODO
 */
public interface SearchByLanguageInputBoundary {

    // TODO: is this necessary???
    /**
     * Executes all necessary operations to load all languages for when the first screen of the view is shown.
     */
    void languageOptions();

    /**
     * Executes the Search by Language use case.
     * @param inputData the input data TODO
     */
    void execute(SearchByLanguageInputData inputData);
}
