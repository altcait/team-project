package use_case.search.bycurrency;

import entity.Country;
import java.util.List;

/**
 * Data access interface for the Search by Currency use case.
 * Provides all countries needed for filtering by currency.
 */
public interface SearchByCurrencyDataAccessInterface {

    /**
     * Returns a list of all independent countries.
     */
    List<Country> getAllCountries();
}
