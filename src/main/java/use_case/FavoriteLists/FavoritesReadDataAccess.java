package use_case.FavoriteLists;

import java.util.List;
import java.util.Map;

/**
 * Read-only access for user favorites (lists + country data)
 * stored in favoritesRepository.json.
 */
public interface FavoritesReadDataAccess {

    /**
     * Get all list names for a user.
     */
    List<String> getUserLists(String username);

    /**
     * Get details of a list (description + countries).
     */
    Map<String, Object> getListDetails(String username, String listName);

    /**
     * Get all countries inside a list.
     */
    List<String> getCountriesInList(String username, String listName);

    /**
     * Get only the description for a list.
     */
    String getListDescription(String username, String listName);
}
