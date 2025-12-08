package use_case.search.by_currency;

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
