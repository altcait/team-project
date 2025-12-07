package use_case.search.bycurrency;

import entity.Country;
import java.util.List;

/**
 * Data access interface for the Search by currency use case.
 * It provides all countries needed for currency filtering.
 */
public interface SearchByCurrencyDataAccessInterface {

    /**
     * Returns a list of all independent countries.
     */
    List<Country> getAllCountries();

}
