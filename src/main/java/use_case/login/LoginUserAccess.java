package use_case.login;

import entity.User;

public interface LoginUserAccess {

    boolean existsByName(String username);

    void save(User user);

    User get(String username);

    void setCurrentUsername(String name);

    String getCurrentUsername();
}
