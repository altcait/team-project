package use_case.search.ByLanguage;

/**
 * Output boundary for the Search by Language use case implemented by TODO:SearchByLanguagePresenter.
 */
public interface SearchByLanguageOutputBoundary {

    // TODO: is this necessary???
    /**
     * Prepares the view when we need the full list of languages that can be queried for the Search by Language use case.
     * @param outputData the output data
     */
    void presentLanguages(SearchByLanguageOutputData outputData);

    /**
     * Prepares the success view when we have a filtered list of countries for the Search by Language use case.
     * @param outputData the output data
     */
    void presentCountries(SearchByLanguageOutputData outputData);

    /**
     * Prepares the failure view for the Search by Language use case when:
     * - no language was selected
     * - no countries were found for selected language
     * - TODO: any more failures?
     * @param errorMessage the error message presented to the user explaining the cause of failure
     */
    void prepareFailView(String errorMessage);
    // TODO: additional fail views / break down fail views into different methods?

    // TODO: "back" from Search view(s)
//    /**
//     * Switches to previous view.
//     */
//    void switchToPreviousView();
}
