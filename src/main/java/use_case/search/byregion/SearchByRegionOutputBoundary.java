package use_case.search.byregion;

/**
 * Output boundary for the Search by region use case.
 * The presenter of Search by region use case implements this interface.
 */
public interface SearchByRegionOutputBoundary {

    /**
     * Called when we have a list of countries to show
     * (for search-by-region or search-by-region-and-subregion).
     */
    void presentCountries(SearchByRegionOutputData outputData);

    /**
     * Called when we have a list of subregions for a given region.
     */
    void presentSubregions(SearchByRegionOutputData outputData);

    /**
     * Called when we have a list of all regions.
     */
    void presentRegions(SearchByRegionOutputData outputData);

    /**
     * Called when something goes wrong (invalid input, no results, etc.).
     */
    void prepareFailView(String errorMessage);

    /**
     * Called when the user want to add selected country to the user's list.
     */
    void switchToSaveCountryView();

    /**
     * Called when the user want to back to user's profile.
     */
    void switchToSelectedListView();
}