package interface_adapter.search.byregion;

import use_case.search.byregion.SearchByRegionInputBoundary;
import use_case.search.byregion.SearchByRegionInputData;

/**
 * Controller for the Search by Region use case.
 * Called by the view in response to user actions.
 */
public class SearchByRegionController {

    private final SearchByRegionInputBoundary interactor;

    public SearchByRegionController(SearchByRegionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadRegions() {
        // Called when we need to load all regions (e.g., when the view is first shown)
        interactor.listRegions();
    }

    public void onRegionSelected(String region) {
        // Called when the user selects a region from the dropdown
        SearchByRegionInputData inputData = new SearchByRegionInputData(region, null);
        interactor.listSubregionsForRegion(inputData);
    }

    public void onSearch(String region, String subregion) {
        if (region == null) {
            // null case
            interactor.searchCountriesByRegion(
                    new SearchByRegionInputData(null, null));
            return;
        }

        if (subregion == null || subregion.isEmpty()) {
            // only search by region
            SearchByRegionInputData inputData =
                    new SearchByRegionInputData(region, null);
            interactor.searchCountriesByRegion(inputData);
        } else {
            // search by both region and subregion
            SearchByRegionInputData inputData =
                    new SearchByRegionInputData(region, subregion);
            interactor.searchCountriesByRegionAndSubregion(inputData);
        }

    }

    public void onAddCountryButtonClicked() {
        // Called when the user clicks the add country button
        interactor.switchToSaveCountryView();
    }

    public void onBackToProfileButtonClicked() {
        // Called when the user clicks the back to profile button
        interactor.switchToProfileView();
    }
}
