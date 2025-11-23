package entity;

import java.util.List;

public class UserFactory {

    public User create(String username, String password, List<String> favouriteCountries) {
        return new User(username, password,  favouriteCountries);
    }
}
