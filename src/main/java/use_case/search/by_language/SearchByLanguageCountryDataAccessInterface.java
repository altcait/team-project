package use_case.search.by_language;

import java.util.List;
import entity.Country;    // TODO: get updated Country entity from remote repo

/**
 * Data access interface for the Search by Language use case providing all countries from the API.
 */
public interface SearchByLanguageCountryDataAccessInterface {
    /**
     * Retrieves a list of all countries in the API.
     */
     List<Country> getAllCountries();    // TODO: get updated Country entity from remote repo
}
