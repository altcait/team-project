package use_case.search.byregion;

import entity.Country;
import java.util.*;

/**
 * Interactor for the Search by region use case.
 */
public class SearchByRegionInteractor implements SearchByRegionInputBoundary {

    private final SearchByRegionDataAccessInterface dataAccess;
    private final SearchByRegionOutputBoundary presenter;

    public SearchByRegionInteractor(SearchByRegionDataAccessInterface dataAccess,
                                    SearchByRegionOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    /**
     * Searching countries by region: return all the countries under the region.
     */
    @Override
    public void searchCountriesByRegion(SearchByRegionInputData inputData) {
        String region = inputData.getRegion();
        if (region == null) {
            presenter.prepareFailView("Region must not be empty.");
            return;
        }

        List<Country> allCountries = dataAccess.getAllCountries();
        List<Country> Countries = new ArrayList<>();

        for (Country country : allCountries) {
            String countryRegion = country.getRegion();
            if (countryRegion != null && countryRegion.equals(region)) {
                Countries.add(country);
            }
        }

        if (Countries.isEmpty()) {
            presenter.prepareFailView("No countries found for region: " + region);
        } else {
            SearchByRegionOutputData outputData =
                    new SearchByRegionOutputData(region, null, Countries, null, null);
            presenter.presentCountries(outputData);
        }
    }

    /**
     * Listing subregions for a region: return all subregions under the region.
     */
    @Override
    public void listSubregionsForRegion(SearchByRegionInputData inputData) {
        String region = inputData.getRegion();
        if (region == null) {
            presenter.prepareFailView("Region must not be empty.");
            return;
        }

        List<Country> allCountries = dataAccess.getAllCountries();
        Set<String> subregionsSet = new HashSet<>();

        for (Country country : allCountries) {
            String countryRegion = country.getRegion();
            if (countryRegion != null && countryRegion.equals(region)) {
                String sub = country.getSubregion();
                if (sub != null) {
                    subregionsSet.add(sub);
                }
            }
        }

        if (subregionsSet.isEmpty()) {
            presenter.prepareFailView("No subregions found for region: " + region);
        } else {
            // Convert it to a List and sort it for view
            List<String> subregionslist = new ArrayList<>(subregionsSet);
            Collections.sort(subregionslist);

            SearchByRegionOutputData outputData =
                    new SearchByRegionOutputData(region, null, null, subregionslist, null);
            presenter.presentSubregions(outputData);
        }
    }

    /**
     * Searching countries by region and subregion: return the corresponding countries.
     */
    @Override
    public void searchCountriesByRegionAndSubregion(SearchByRegionInputData inputData) {
        String region = inputData.getRegion();
        String subregion = inputData.getSubregion();

        if (region == null) {
            presenter.prepareFailView("Region must not be empty.");
            return;
        }
        if (subregion == null) {
            presenter.prepareFailView("Subregion must not be empty.");
            return;
        }

        List<Country> allCountries = dataAccess.getAllCountries();
        List<Country> matchedCountries = new ArrayList<>();

        for (Country country : allCountries) {
            String countryRegion = country.getRegion();
            String countrySubregion = country.getSubregion();

            if (countryRegion != null && countrySubregion != null
                    && countryRegion.equals(region)
                    && countrySubregion.equals(subregion)) {
                matchedCountries.add(country);
            }
        }

        if (matchedCountries.isEmpty()) {
            presenter.prepareFailView("No countries found for region "
                    + region + " and subregion " + subregion + ".");
        } else {
            SearchByRegionOutputData outputData =
                    new SearchByRegionOutputData(region, subregion, matchedCountries, null, null);
            presenter.presentCountries(outputData);
        }
    }

    /**
     * Listing all regions: return all existing regions.
     */
    @Override
    public void listRegions() {
        List<Country> allCountries = dataAccess.getAllCountries();
        Set<String> regionsSet = new HashSet<>();

        for (Country country : allCountries) {
            String region = country.getRegion();
            if (region != null) {
                regionsSet.add(region);
            }
        }

        if (regionsSet.isEmpty()) {
            presenter.prepareFailView("No regions found.");
        } else {
            List<String> regions = new ArrayList<>(regionsSet);
            Collections.sort(regions);

            SearchByRegionOutputData outputData =
                    new SearchByRegionOutputData(null, null, null, null, regions);
            presenter.presentRegions(outputData);
        }
    }

    /**
     * Listing when click the add button.
     */
    @Override
    public void switchToSaveCountryView() {
        presenter.switchToSaveCountryView();
    }

    /**
     * Listing when click the back to profile button.
     */
    @Override
    public void switchToProfileView() {
        presenter.switchToProfileView();
    }
}