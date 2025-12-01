package data_access;

import org.json.JSONException;
import use_case.FavoritesList.FavoritesReadDataAccess;
import use_case.save_country.SaveCountryDataAccessInterface;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserDataAccessObject implements SaveCountryDataAccessInterface, FavoritesReadDataAccess {
    private final File jsonFile;
    private final Map<String, Map<String, Map<String, Object>>> favouritesByUser = new HashMap<>();

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

                Map<String, Map<String, Object>> listsWithCountries = new HashMap<>();

                for (String listName : userLists.keySet()) {
                    JSONObject listDetails = userLists.getJSONObject(listName);
                    JSONObject countriesList = listDetails.getJSONObject("countries");
                    String listDescription = "";

                    if (listDetails.has("description")) {
                        listDescription = listDetails.getString("description");
                    }

                    Map<String, Object> details = new HashMap<>();

                    Map<String, String> countriesWithNotes = new HashMap<>();

                    for (String countryCode : countriesList.keySet()) {
                        countriesWithNotes.put(countryCode, countriesList.getString(countryCode));
                    }
                    details.put("description", listDescription);
                    details.put("countries", countriesWithNotes);
                    listsWithCountries.put(listName, details);
                }
                this.favouritesByUser.put(username, listsWithCountries);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureJsonExists() throws RuntimeException {
        Path jsonFilePath = Paths.get(jsonFile.getAbsolutePath());
        // If file doesn't exist, create it and initialize it with empty json brackets
        if (!Files.exists(jsonFilePath)) {
            try {
                Files.createFile(jsonFilePath);
                Files.writeString(jsonFilePath, "{}");
            } catch (IOException e) {
                throw new RuntimeException("Failed to create json file", e);
            }
        }
        // If file exists but is empty, add empty json brackets
        try {
            if (Files.size(jsonFilePath) == 0) {
                Files.writeString(jsonFilePath, "{}");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean userExists(String username) {
        return favouritesByUser.containsKey(username.toLowerCase());
    }

    @Override
    public boolean listExists(String username, String listName) {
        Map<String, Map<String, Object>> listsWithDetails = favouritesByUser.get(username.toLowerCase());
        if (listsWithDetails == null) {
            return false;
        }
        return listsWithDetails.containsKey(listName.toLowerCase());
    }

    @Override
    public boolean countryExists(String username, String listName, String countryCode) {
        // return early if user or list don't exist
        if (!userExists(username) || !listExists(username, listName)) {
            return false;
        }

        Map<String, Map<String, Object>> listsWithDetails = favouritesByUser.get(username.toLowerCase());
        Map<String, Object> listContents = listsWithDetails.get(listName.toLowerCase());
        Map<String, String> countriesList = (Map<String, String>)listContents.get("countries");
        return countriesList.containsKey(countryCode.toUpperCase());
    }

    @Override
    public void addUser(String username) {
        // Add user to file
        favouritesByUser.put(username.toLowerCase(), new HashMap<>());
    }

    @Override
    public void addList(String username, String listName) {
        Map<String, Map<String, Object>> listsWithCountries = favouritesByUser.get(username.toLowerCase());
        // Add list to username's list dictionary
        listsWithCountries.put(listName.toLowerCase(), new HashMap<>());
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
        Map<String, Map<String, Object>> listsWithDetails = favouritesByUser.get(username.toLowerCase());
        Map<String, Object> listContents = listsWithDetails.get(listName.toLowerCase());

        // Add "countries" key if it's not yet present in the list
        if (!listContents.containsKey("countries")) {
            listContents.put("countries", new HashMap<>());
        }

        Map<String, String> countriesWithNotes = (Map<String, String>)listContents.get("countries");
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

    // ============================================
    // FavoritesReadDataAccess Interface Methods
    // ============================================

    @Override
    public List<String> getUserLists(String username) {
        Map<String, Map<String, Object>> userLists = favouritesByUser.get(username.toLowerCase());
        if (userLists == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(userLists.keySet());
    }

    @Override
    public Map<String, Object> getListDetails(String username, String listName) {
        Map<String, Map<String, Object>> userLists = favouritesByUser.get(username.toLowerCase());
        if (userLists == null) {
            return null;
        }
        return userLists.get(listName.toLowerCase());
    }

    @Override
    public List<String> getCountriesInList(String username, String listName) {
        Map<String, Object> listDetails = getListDetails(username, listName);
        if (listDetails == null) {
            return new ArrayList<>();
        }

        @SuppressWarnings("unchecked")
        Map<String, String> countries = (Map<String, String>) listDetails.get("countries");
        if (countries == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(countries.keySet());
    }

    @Override
    public String getListDescription(String username, String listName) {
        Map<String, Object> listDetails = getListDetails(username, listName);
        if (listDetails == null) {
            return "";
        }
        return (String) listDetails.getOrDefault("description", "");
    }
}