package interface_adapter.profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import use_case.profile.ProfileOutputBoundary;
//import interface_adapter.lists.ViewSavedListsViewModel;

public class ProfilePresenter implements ProfileOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;
    private final ProfileViewModel profileViewModel;
    //private final ViewSavedListsViewModel savedViewModel;

    public ProfilePresenter(ViewManagerModel viewManagerModel,
                            LoginViewModel loginViewModel,
                            ProfileViewModel profileViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.profileViewModel = profileViewModel;
    }


    @Override
    public void switchToFavoritesView() {
        //body
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
