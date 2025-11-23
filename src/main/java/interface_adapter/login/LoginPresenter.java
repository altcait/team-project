package interface_adapter.login;

import use_case.login.LoginOutput;
import use_case.login.LoginOutputBoundary;

public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel viewModel;

    public LoginPresenter(LoginViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccess(LoginOutput loginOutput) {
        LoginState newState = new LoginState();

        newState.setUsername(loginOutput.getDisplayName());
        newState.setLoginError("");
        viewModel.setState(newState);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailure(String errorMessage) {
        LoginState newState = new LoginState();
        newState.setLoginError("");
        viewModel.setState(newState);
        viewModel.firePropertyChange();
    }
}
