package entity;

import java.util.Map;

public class User {

    private final String name;
    private final String password;
    private final Map<String, Map<String, Object>> favouriteCountries;

    private String language;
    private String bio;

    public User(String name, String password) {
        this(name, password, null);
    }

    public User(String name, String password,
                Map<String, Map<String, Object>> favouriteCountries) {

        if ("".equals(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        this.name = name;
        this.password = password;
        this.favouriteCountries =
                favouriteCountries != null ? favouriteCountries : Map.of();

        this.language = "";
        this.bio = "";
    }

    public String getName() { return name; }
    public String getPassword() { return password; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Map<String, Map<String, Object>> getFavouriteCountries() {
        return favouriteCountries;
    }
}
