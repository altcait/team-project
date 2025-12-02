package use_case.search.by_region;

import entity.Country;

import java.util.List;

/**
 * Output data for the Search by region use case.
 */

public class SearchByRegionOutputData {

    private final String region;
    private final String subregion; // may be null
    private final List<Country> countries;
    private final List<String> subregions;
    private final List<String> regions;

    public SearchByRegionOutputData(String region,
                                    String subregion,
                                    List<Country> countries,
                                    List<String> subregions,
                                    List<String> regions) {
        this.region = region;
        this.subregion = subregion;
        this.countries = countries;
        this.subregions = subregions;
        this.regions = regions;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public List<String> getSubregions() {
        return subregions;
    }

    public List<String> getRegions() {
        return regions;
    }
}