package use_case.search.ByLanguage;

/**
 * Input boundary for the Search by Language use case implemented by SearchByLanguageInteractor.
 *   It supports TODO operations:
 *   1, TODO
 */
public interface SearchByLanguageInputBoundary {

    /**
     * Executes the Search by Language use case.
     * @param searchByLanguageInputData the input data TODO
     */
    void execute(SearchByLanguageInputData searchByLanguageInputData);
}
