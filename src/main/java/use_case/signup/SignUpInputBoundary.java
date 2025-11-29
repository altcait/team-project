package use_case.signup;

public interface SignUpInputBoundary {
    void execute(SignUpInput signUpInput);

    void switchToLoginView();
}
