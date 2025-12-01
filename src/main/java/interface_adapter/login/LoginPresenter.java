package interface_adapter.login;

import data_access.UserCSVDataAccess;
import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import use_case.login.LoginOutput;
import use_case.login.LoginOutputBoundary;

public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final ProfileViewModel profileViewModel;
    private final UserCSVDataAccess userDataAccess;

    public LoginPresenter(LoginViewModel viewModel,
                          ViewManagerModel viewManagerModel,
                          ProfileViewModel profileSupplier,
                          UserCSVDataAccess userDataAccess) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.profileViewModel = profileSupplier;
        this.userDataAccess = userDataAccess;
    }

    @Override
    public void prepareSuccess(LoginOutput loginOutput) {
        String username = loginOutput.getUsername();

        User user = userDataAccess.get(username);

        ProfileState profileState = profileViewModel.getState();
        profileState.setUsername(username);
        profileState.setLanguage(user.getLanguage());
        profileState.setBio(user.getBio());

        profileViewModel.setState(profileState);
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
