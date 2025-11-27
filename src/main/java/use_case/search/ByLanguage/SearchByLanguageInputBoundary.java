package use_case.search.ByLanguage;

/**
 * Input boundary for the Search by Language use case implemented by SearchByLanguageInteractor.
 *   It supports TODO operations:
 *   1, TODO
 */
public interface SearchByLanguageInputBoundary {

    // TODO: is this necessary??? this doesn't feel like input boundary / interactor material -- perhaps a Languages
    //  entity could be good using a composition/has-a relationship with the Country entity.
    /**
     * Executes all necessary operations to load all languages for when the first screen of the view is shown.
     */
    void languageOptions();

    // TODO: "back" from Search view(s)
//    /**
//     * Executes the "return to previous view" use case(?).
//     */
//    void switchToPreviousView();

    /**
     * Executes the Search by Language use case.
     * @param inputData the input data TODO
     */
    void execute(SearchByLanguageInputData inputData);
}
