package interface_adapter.search.by_region;

import entity.Country;

import java.util.List;

/**
 * State object for the Search by Region view.
 * Holds all data that the UI needs to display with.
 */
public class SearchByRegionState {

    private String selectedRegion;
    private String selectedSubregion;
    private List<String> regionOptions;
    private List<String> subregionOptions;
    private List<Country> countries;
    private String errorMessage;

    public String getSelectedRegion() {
        // this is used by Controller to get the region selected by user
        return selectedRegion;
    }

    public void setSelectedRegion(String selectedRegion) {
        // this is used by Controller to get the region selected by user
        this.selectedRegion = selectedRegion;
    }

    public String getSelectedSubregion() {
        return selectedSubregion;
    }

    public void setSelectedSubregion(String selectedSubregion) {
        this.selectedSubregion = selectedSubregion;
    }

    public List<String> getRegionOptions() {
        return regionOptions;
    }

    public void setRegionOptions(List<String> regionOptions) {
        this.regionOptions = regionOptions;
    }

    public List<String> getSubregionOptions() {
        return subregionOptions;
    }

    public void setSubregionOptions(List<String> subregionOptions) {
        this.subregionOptions = subregionOptions;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}