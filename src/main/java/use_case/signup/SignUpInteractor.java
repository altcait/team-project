package use_case.signup;

import entity.User;
import entity.UserFactory;
import java.util.ArrayList;
import java.util.HashMap;

public class SignUpInteractor implements SignUpInputBoundary {
    private final SignUpUserAccess userAccess;
    private final SignUpOutputBoundary presenter;
    private final UserFactory userFactory;

    public SignUpInteractor(SignUpUserAccess userAccess, SignUpOutputBoundary presenter, UserFactory userFactory) {
        this.userAccess = userAccess;
        this.presenter = presenter;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignUpInput input) {
        if (input.getUsername().isEmpty()) {
            presenter.prepareFailureView("Username cannot be empty.");
            return;
        }

        if (input.getPassword().isEmpty()) {
            presenter.prepareFailureView("Password cannot be empty.");
            return;
        }

        if (userAccess.existsByName(input.getUsername())) {
            presenter.prepareFailureView("Username already exists.");
            return;
        }

        User newUser = userFactory.create(input.getUsername(), input.getPassword(), new HashMap<>());
        userAccess.save(newUser);

        presenter.prepareSuccessView(new SignUpOutput(input.getUsername()));
    }

    @Override
    public void switchToLoginView() {
        presenter.switchToLoginView();
    }
}
