package use_case.login;

public class LoginOutput {

    private final String displayName;
    public LoginOutput(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return displayName;
    }
}
