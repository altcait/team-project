package use_case.login;

public class LoginInput {

    private String username;
    private String password;

    public LoginInput(String username, String password) {
        this.username = username;
        this.password = password;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }
}
