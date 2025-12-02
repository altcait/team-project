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