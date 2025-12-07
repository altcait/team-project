package use_case.search.by_region;

/**
 * Input data for the Search by region use case.
 * Wraps the region and subregion (optionally) from the user.
 */
public class SearchByRegionInputData {

    private final String region;
    private final String subregion; // may be null or empty for some operations

    public SearchByRegionInputData(String region, String subregion) {
        this.region = region;
        this.subregion = subregion;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }
}