package entity;

import java.util.Map;

public class User {

    private final String name;
    private final String password;
    private final Map<String, Map<String, Object>> favouriteCountries;

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
    }

    public String getName() { return name; }
    public String getPassword() { return password; }
    public Map<String, Map<String, Object>> getFavouriteCountries() {
        return favouriteCountries;
    }
}
