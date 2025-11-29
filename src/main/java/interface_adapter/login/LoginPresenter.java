package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.profile.ProfileViewModel;
import use_case.login.LoginOutput;
import use_case.login.LoginOutputBoundary;
import java.util.function.Supplier;

public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final Supplier<ProfileViewModel> profileSupplier;

    public LoginPresenter(LoginViewModel viewModel,
                          ViewManagerModel viewManagerModel,
                          Supplier<ProfileViewModel> profileSupplier) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.profileSupplier = profileSupplier;
    }

    @Override
    public void prepareSuccess(LoginOutput loginOutput) {
        ProfileViewModel profileViewModel = profileSupplier.get();
        profileViewModel.getState().setUsername(loginOutput.getDisplayName());
        profileViewModel.firePropertyChange();

        viewManagerModel.setState("profile");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailure(String errorMessage) {
        LoginState newState = new LoginState();
        newState.setLoginError(errorMessage);
        viewModel.setState(newState);
        viewModel.firePropertyChange();
    }
}
