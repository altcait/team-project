package interface_adapter.profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import use_case.profile.ProfileOutputBoundary;
//import interface_adapter.lists.ViewSavedListsViewModel;

public class ProfilePresenter implements ProfileOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;
    //private final ViewSavedListsViewModel savedViewModel;

    public ProfilePresenter(ViewManagerModel viewManagerModel, LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
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
        //body
    }
}
