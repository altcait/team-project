package use_case.search.ByLanguage;

/**
 * Input data for the Search by Language use case.
 */
public class SearchByLanguageInputData {
    private final String language;

    public SearchByLanguageInputData(String language) {
        this.language = language;
    }

    String getLanguage() {
        return language;
    }
}
