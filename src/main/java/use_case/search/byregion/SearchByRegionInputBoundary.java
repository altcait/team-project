package use_case.search.byregion;

/**
 * Input boundary for the Search by region use case.
 * It supports four operations:
 *   1, Search countries by specific region
 *   2, List subregions for a specific region
 *   3, Search countries by region and subregion
 *   4, List all regions (for initializing)
 */
public interface SearchByRegionInputBoundary {

    /** 1, Return all the countries under the region based on the region.
     */
    void searchCountriesByRegion(SearchByRegionInputData inputData);

    /** 2, Return all the subregions in the region based on the region (after deduplication).
     */
    void listSubregionsForRegion(SearchByRegionInputData inputData);

    /** 3, Return the list of countries based on region and subregion.
     */
    void searchCountriesByRegionAndSubregion(SearchByRegionInputData inputData);

    /** 4, Return the list of regions (after deduplication)
     */
    void listRegions();
}
