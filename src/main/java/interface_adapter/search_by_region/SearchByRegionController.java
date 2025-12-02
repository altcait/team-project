package interface_adapter.search_by_region;

import use_case.search.by_region.SearchByRegionInputBoundary;
import use_case.search.by_region.SearchByRegionInputData;

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
        interactor.listRegions();
    }

    public void onRegionSelected(String region) {
        SearchByRegionInputData inputData = new SearchByRegionInputData(region, null);
        interactor.listSubregionsForRegion(inputData);
    }

    public void onSearch(String region, String subregion) {
        if (region == null) {
            interactor.searchCountriesByRegion(
                    new SearchByRegionInputData(null, null));
            return;
        }

        if (subregion == null || subregion.isEmpty()) {
            SearchByRegionInputData inputData =
                    new SearchByRegionInputData(region, null);
            interactor.searchCountriesByRegion(inputData);
        } else {
            SearchByRegionInputData inputData =
                    new SearchByRegionInputData(region, subregion);
            interactor.searchCountriesByRegionAndSubregion(inputData);
        }
    }

    public void onAddCountryButtonClicked() {
        interactor.switchToSaveCountryView();
    }

    public void onBackToSelectedListButtonClicked() {
        interactor.switchToSelectedListView();
    }
}