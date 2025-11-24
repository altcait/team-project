package use_case.signup;

import entity.User;

public interface SignUpUserAccess {
    boolean existsByName(String username);

    void save(User user);
}
