package data_access;

import org.json.JSONException;
import use_case.save_country.SaveCountryDataAccessInterface;

import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileUserDataAccessObject implements SaveCountryDataAccessInterface {
    private final File jsonFile;
    private final Map<String, Map<String, Map<String, String>>> favouritesByUser = new HashMap<>();

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String jsonPath) {
        jsonFile = new File(jsonPath);
        populateFavouritesByUser();
    }

    private void populateFavouritesByUser() {
//    JSON is of the format:
//    {
//        "caitlinhen001": {
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
        favouriteCountriesWithNotes.put("USA", "fun!");
        favouriteCountriesWithNotes.put("CAN", "beautiful!");
        favouriteCountriesWithNotes.put("HKG", "been before!");

        // List with countries from above:
        Map<String, Map<String, String>> listsWithCountries = new HashMap<>();
        listsWithCountries.put("visited", favouriteCountriesWithNotes);

        // Username with associated favs:
        this.favouritesByUser.put("caitlinhen001", listsWithCountries);
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
        // return early if user or list don't exist
        if (!userExists(username) || !listExists(username, listName)) {
            return false;
        }

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
        // add user if they don't yet exist in the structure
        if (!userExists(username)) {
            addUser(username);
        }
        // add list if it doesn't exist for the given user
        if (!listExists(username, listName)) {
            addList(username, listName);
        }

        // Update favouritesByUser to include the new country
        Map<String, Map<String, String>> listsWithCountries = favouritesByUser.get(username);
        Map<String, String> countriesWithNotes = listsWithCountries.get(listName);
        countriesWithNotes.put(countryCode, notes);
        // save updated favourtiesByUser object to file
        save();
    }

    @Override
    public void save() {
        JSONObject favourites = new JSONObject();
        JSONObject lists = new JSONObject();
        JSONObject countriesWithNotes = new JSONObject();

        // build json object for writing to file
        favouritesByUser.forEach((username, favouritesLists) -> {
            favouritesLists.forEach((listName, countriesMap) -> {
                countriesMap.forEach((countryName, notes) -> {
                    countriesWithNotes.put(countryName, notes);
                });
                lists.put(listName, countriesWithNotes);
            });
            favourites.put(username, lists);
        });

        try {
            FileWriter fileWriter = new FileWriter(jsonFile.getAbsolutePath());

            fileWriter.write(favourites.toString());
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
