package use_case.search.ByLanguage;

import java.util.List;
import entity.Country;    // TODO: get updated Country entity from remote repo

/**
 * Data access interface for the Search by Language use case providing all countries from the API.
 */
public interface SearchByLanguageCountryDataAccessInterface {
    // TODO: ApiSearchByRegionDataAccessObject (also) implements SearchByLanguageCountryDataAccessInterface
    /**
     * Retrieves a list of all countries in the API.
     */
     List<Country> getAllCountries();    // TODO: get updated Country entity from remote repo

    // TODO: may be uneccessary
    /**
     * Retrieves a single country by its cca3 code.
     * @param cca3 the cca3 code to be queried
     * @return a Country entity corresponding to the queried code or null if the code doesn't correspond to a country
     */
    Country getCountryByCca3(String cca3);
}
