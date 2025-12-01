package interface_adapter.profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import use_case.profile.ProfileOutputBoundary;

public class ProfilePresenter implements ProfileOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;
    private final ProfileViewModel profileViewModel;

    public ProfilePresenter(ViewManagerModel viewManagerModel,
                            LoginViewModel loginViewModel,
                            ProfileViewModel profileViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void switchToFavoritesView() {
        // Go to the "My Saved Lists" screen
        // "lists" MUST match ListsView.viewName
        viewManagerModel.setState("lists");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToEditProfileView() {
        viewManagerModel.setState("edit-profile");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToProfileView() {
        viewManagerModel.setState("profile");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void updateProfile(String language, String bio) {
        ProfileState state = profileViewModel.getState();
        state.setLanguage(language);
        state.setBio(bio);

        profileViewModel.setState(state);
        profileViewModel.firePropertyChange();
    }
}
