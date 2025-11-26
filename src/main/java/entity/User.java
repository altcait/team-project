package entity;

import java.util.Map;

public class User {

    private final String name;
    private final String password;
    private final Map<String, Map<String, String>> favouriteCountries;

    public User(String name, String password, Map<String, Map<String, String>> favouriteCountries) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
        this.favouriteCountries = favouriteCountries;
    }
    public String getName() {return name;}
    public String getPassword() {return password;}
    public Map<String, Map<String, String>> getFavouriteCountries() {return favouriteCountries;}
}
