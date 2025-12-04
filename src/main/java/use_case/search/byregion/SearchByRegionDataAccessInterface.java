package use_case.search.byregion;

import entity.Country;
import java.util.List;

/**
 * Data access interface for the Search by region use case.
 * It provides all countries needed for region (and subregion) filtering.
 */
public interface SearchByRegionDataAccessInterface {

    /**
     * Returns a list of all independent countries.
     */
    List<Country> getAllCountries();
}