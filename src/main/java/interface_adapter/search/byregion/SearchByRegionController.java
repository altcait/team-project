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

        // If you want to display all the countries under a region directly when you select it,
        // you can also adjust this sentence incidentally
        // interactor.searchCountriesByRegion(inputData);
    }

    public void onSearchByRegionAndSubregion(String region, String subregion) {
        // Called when the user clicks search with both region and subregion selected
        SearchByRegionInputData inputData = new SearchByRegionInputData(region, subregion);
        interactor.searchCountriesByRegionAndSubregion(inputData);
    }
}
