package use_case.signup;

public class SignUpInput {
    private final String username;
    private final String password;

    public SignUpInput(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
