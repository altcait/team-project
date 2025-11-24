package interface_adapter.login;

import use_case.login.LoginInput;
import use_case.login.LoginInputBoundary;

public class LoginController {

    private final LoginInputBoundary loginUseCaseInteractor;

    public LoginController(LoginInputBoundary loginUseCaseInteractor) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
    }

    public void execute(String username, String password) {
        final LoginInput loginInput = new LoginInput(username, password);
        loginUseCaseInteractor.execute(loginInput);
    }

}
