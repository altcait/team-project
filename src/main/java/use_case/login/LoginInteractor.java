package use_case.login;

import entity.User;

public class LoginInteractor implements LoginInputBoundary {

    private final LoginUserAccess userAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserAccess userAccess, LoginOutputBoundary loginOutputBoundary) {
        this.userAccessObject = userAccess;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInput loginInput) {
        final String username = loginInput.getUsername();
        final String password = loginInput.getPassword();
        if (!userAccessObject.existsByName(username)) {
            loginPresenter.prepareFailure(username + ": Account does not exist");
        }
        else {
            final String pword = userAccessObject.get(username).getPassword();
            if (!password.equals(pword)) {
                loginPresenter.prepareFailure(username + ": Incorrect password for \" " + username + "\".");
            }
            else {
                final User user = userAccessObject.get(loginInput.getUsername());
                userAccessObject.setCurrentUsername(username);
                final LoginOutput loginOutput = new LoginOutput(user.getName());
                loginPresenter.prepareSuccess(loginOutput);
            }
        }
    }
}
