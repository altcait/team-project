package use_case.search.ByLanguage;

/**
 * Use Case Interactor for the Search by Language use case.
 * Main flow: TODO
 * 1. searching countries by language
 * 2. listing all regions
 */
public class SearchByLanguageInteractor implements SearchByLanguageInputBoundary {
    private final SearchByLanguageCountryDataAccessInterface countryDataAccess;
    private final SearchByLanguageOutputBoundary searchByLanguagePresenter;

    public SearchByLanguageInteractor(SearchByLanguageCountryDataAccessInterface countryDataAccess, SearchByLanguageOutputBoundary searchByLanguagePresenter) {
        this.countryDataAccess = countryDataAccess;
        this.searchByLanguagePresenter = searchByLanguagePresenter;
    }
    // TODO
}
