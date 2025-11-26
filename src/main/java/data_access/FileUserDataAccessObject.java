package data_access;

import org.json.JSONException;
import use_case.save_country.SaveCountryDataAccessInterface;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        loadFavouritesFromFile();
    }

    private void loadFavouritesFromFile() {
        ensureJsonExists();

        try {
            // Read file contents into a String
            String jsonString = Files.readString(jsonFile.toPath());
            // Create JSONObject from resulting String
            JSONObject myObject = new JSONObject(jsonString);

            // Populate favouritesByUser HashMap by looping through JSONObject
            for (String username : myObject.keySet()) {
                JSONObject userLists = myObject.getJSONObject(username);
                Map<String, Map<String, String>> listsWithCountries = new HashMap<>();

                for (String listName : userLists.keySet()) {
                    JSONObject countriesList = userLists.getJSONObject(listName);
                    Map<String, String> countriesWithNotes = new HashMap<>();

                    for (String countryCode : countriesList.keySet()) {
                        countriesWithNotes.put(countryCode, countriesList.getString(countryCode));
                    }
                    listsWithCountries.put(listName, countriesWithNotes);
                }
                this.favouritesByUser.put(username, listsWithCountries);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureJsonExists() {
        Path jsonFilePath = Paths.get(jsonFile.getAbsolutePath());
        if (!Files.exists(jsonFilePath)) {
            try {
                Files.createFile(jsonFilePath);
                Files.write(jsonFilePath, "".getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create json file", e);
            }
        }
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
        return countriesWithNotes.containsKey(countryCode.toUpperCase());
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
        countriesWithNotes.put(countryCode.toUpperCase(), notes);
        // save updated favouritesByUser object to file
        save();
    }

    @Override
    public void save() {
        JSONObject favourites = new JSONObject(favouritesByUser);

        try {
            FileWriter fileWriter = new FileWriter(jsonFile.getAbsolutePath());

            fileWriter.write(favourites.toString());
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
