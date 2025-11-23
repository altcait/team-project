package data_access;

import use_case.save_country.SaveCountryDataAccessInterface;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileUserDataAccessObject implements SaveCountryDataAccessInterface {
//    private final File jsonFile;
    Map<String, Map<String, Map<String, String>>> favouritesByUser = new HashMap<>();

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String jsonPath) {
//    JSON is of the format:
//    {
//        "caitlin001": {
//            "visited": { "CAN": "beautiful mountains", "USA": "mehhhh" },
//            "wishlist": { "JPN": "want to go for cherry blossoms" }
//        },
//        "anonuser": {
//            "visited": { "FRA": "amazing food" },
//            "wishlist": { "HKG": "beautiful city" }
//        }
//    }

        // Countries and notes:
        Map<String, String> favouriteCountriesWithNotes = new HashMap<>();
        favouriteCountriesWithNotes.put("USA", "nahhhh");
        favouriteCountriesWithNotes.put("CAN", "beautiful!");
        favouriteCountriesWithNotes.put("HKG", "been before!");

        // List with countries from above:
        Map<String, Map<String, String>> listsWithCountries = new HashMap<>();
        listsWithCountries.put("visited", favouriteCountriesWithNotes);

        // Username with associated favs:
        favouritesByUser.put("caitlinhen001", listsWithCountries);
    }

    @Override
    public boolean userExists(String username) {
        return favouritesByUser.containsKey(username);
    }

    @Override
    public boolean listExists(String username, String listName) {
        Map<String, Map<String, String>> listsWithCountries = favouritesByUser.get(username);
        return listsWithCountries.containsKey(listName);
    }

    @Override
    public boolean countryExists(String username, String listName, String countryCode) {
        Map<String, Map<String, String>> listsWithCountries = favouritesByUser.get(username);
        Map<String, String> countriesWithNotes = listsWithCountries.get(listName);
        return countriesWithNotes.containsKey(countryCode);
    }

    @Override
    public void addUser(String username) {
        // Add user to file
        favouritesByUser.put(username, new HashMap<>());
    }

    @Override
    public void addList(String username, String listName) {
        Map<String, Map<String, String>> listsWithCountries = favouritesByUser.get(username);
        // Add list to username's list dictionary
        listsWithCountries.put(listName, new HashMap<>());
    }

    @Override
    public void addCountry(String username, String listName, String countryCode, String notes) {
        Map<String, Map<String, String>> listsWithCountries = favouritesByUser.get(username);
        Map<String, String> countriesWithNotes = listsWithCountries.get(listName);
        countriesWithNotes.put(countryCode, notes);
    }

}
