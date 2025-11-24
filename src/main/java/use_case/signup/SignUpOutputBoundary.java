package use_case.signup;

public interface SignUpOutputBoundary {
    void prepareSuccessView(SignUpOutput signUpOutput);
    void prepareFailureView(String message);

    void switchToLoginView();
}
