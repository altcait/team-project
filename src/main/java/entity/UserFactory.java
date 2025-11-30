package entity;

import java.util.List;
import java.util.Map;

public class UserFactory {

    public User create(String username, String password) {
        return new User(username, password);
    public User create(String username, String password, Map<String, Map<String, Object>> favouriteCountries) {
        return new User(username, password,  favouriteCountries);
    }
}
