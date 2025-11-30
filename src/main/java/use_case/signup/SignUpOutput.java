package use_case.signup;

public class SignUpOutput {
    private final String username;

    public SignUpOutput(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
