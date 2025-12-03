package use_case.search.by_region;

/**
 * Input boundary for the Search by region use case.
 * The interactor of Search by region use case implements this interface.
 */
public interface SearchByRegionInputBoundary {

    /** Return all the countries under the region based on the region.
     */
    void searchCountriesByRegion(SearchByRegionInputData inputData);

    /** Return all the subregions in the region based on the region (after deduplication).
     */
    void listSubregionsForRegion(SearchByRegionInputData inputData);

    /** Return the list of countries based on region and subregion.
     */
    void searchCountriesByRegionAndSubregion(SearchByRegionInputData inputData);

    /** Return the list of regions (after deduplication)
     */
    void listRegions();


    /** Switch to Save Country View
     */
    void switchToSaveCountryView();

    /** Switch to Profile View
     */
    void switchToSelectedListView();

}
