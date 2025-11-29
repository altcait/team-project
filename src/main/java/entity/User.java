package entity;

import java.util.List;

public class User {

    private final String name;
    private final String password;

    public User(String name, String password) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
    }
    public String getName() {return name;}
    public String getPassword() {return password;}
}
