package use_case.search.by_language;

/**
 * Output boundary for the Search by Language use case implemented by SearchByLanguagePresenter.
 */
public interface SearchByLanguageOutputBoundary {

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
     * @param errorMessage the error message presented to the user explaining the cause of failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to previous view.
     */
    void switchToPreviousView();

    void switchToSaveCountryView();
}
