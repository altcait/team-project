package use_case.save_country;

import java.util.List;

/**
 * DAO interface for the Save Country use case
 */
public interface SaveCountryDataAccessInterface {

    /**
     * Checks if the user exists in the File
     * @param username the username to look up
     * @return list of strings containing the user's lists
     */
    List<String> getListNames(String username);

    /**
     * Checks if the user exists in the File
     * @param username the username to look up
     * @return true if the user exists in the File
     */
    boolean userExists(String username);

    /**
     * Checks if the list exists in the user's favourites
     * @param username the username to look up
     * @param listName the list to check
     * @return true if the list exists in the user's favourites
     */
    boolean listExists(String username, String listName);

    /**
     * Checks if the country exists in the user's listName list
     * @param username the username to look up
     * @param countryCode the country code
     * @param listName the list to check
     * @return true if the country exists in the given list
     */
    boolean countryExists(String username, String listName, String countryCode);

    /**
     * Adds the user identified by username to the File
     * @param username the user whose favourites we want to add the list to
     */
    void addUser(String username);

    /**
     * Adds the list identified by listName to the user's favourites
     * @param username the user whose favourites we want to add the list to
     * @param listName the list to add by name
     */
    void addList(String username, String listName);

    /**
     * Adds the country to the user's specified list
     * @param username the user whose list we want to save to
     * @param listName the name of the list to save to
     * @param countryCode the country code to add to the list
     * @param notes the notes to add about that country
     */
    void addCountry(String username, String listName, String countryCode, String notes);

    /**
     * Save favourites repository to json file.
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    void save();
}
