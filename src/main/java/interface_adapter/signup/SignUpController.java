package interface_adapter.signup;

import use_case.signup.SignUpInputBoundary;
import use_case.signup.SignUpInput;

public class SignUpController {
    private final SignUpInputBoundary interactor;
    public SignUpController(SignUpInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username, String password) {
        SignUpInput input = new SignUpInput(username, password);
        interactor.execute(input);
    }

    public void switchToLoginView() {
        interactor.switchToLoginView();
    }
}
