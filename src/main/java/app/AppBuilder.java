package app;

import data_access.UserCSVDataAccess;
import entity.UserFactory;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.*;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserAccess;
import use_case.profile.*;

import view.LoginSignUpView;
import view.ProfileView;
import view.ViewManager;

import interface_adapter.signup.*;
import use_case.signup.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private ViewManager viewManager;

    private LoginSignUpView loginSignUpView;
    private LoginViewModel loginViewModel;
    private SignUpViewModel signUpViewModel;
    private ProfileViewModel profileViewModel;
    private UserCSVDataAccess userDataAccess;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        userDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());
    }

    public AppBuilder addLoginSignUpView() {
        loginViewModel = new LoginViewModel();
        loginSignUpView = new LoginSignUpView(loginViewModel);

        cardPanel.add(loginSignUpView, loginSignUpView.getViewName());
        return this;
    }

    public AppBuilder addLoginUseCase() {

        LoginUserAccess loginDataAccess = userDataAccess;

        LoginOutputBoundary loginPresenter = new LoginPresenter(
                loginViewModel, viewManagerModel, this::getProfileViewModel);
        LoginInputBoundary loginInteractor = new LoginInteractor(loginDataAccess, loginPresenter);

        LoginController loginController = new LoginController(loginInteractor);
        loginSignUpView.setLoginController(loginController);

        return this;
    }

    public AppBuilder addSignUpUseCase() {
        signUpViewModel = new SignUpViewModel();

        SignUpUserAccess signupDataAccess = userDataAccess;

        SignUpOutputBoundary signupPresenter = new SignUpPresenter(viewManagerModel, signUpViewModel, loginViewModel);
        SignUpInputBoundary signupInteractor =
                new SignUpInteractor(signupDataAccess, signupPresenter, new UserFactory());

        SignUpController signupController = new SignUpController(signupInteractor);
        loginSignUpView.setSignupController(signupController);

        return this;
    }

    public AppBuilder addProfileUseCase() {

        profileViewModel = new ProfileViewModel();

        ProfileOutputBoundary profilePresenter = new ProfilePresenter(viewManagerModel, loginViewModel);
        ProfileInputBoundary profileInteractor = new ProfileInteractor(profilePresenter);
        ProfileController profileController = new ProfileController(profileInteractor);
        ProfileView profileView = new ProfileView(profileViewModel);
        profileView.setController(profileController);

        cardPanel.add(profileView, profileView.getViewName());

        return this;
    }

    private ProfileViewModel getProfileViewModel() {
        return profileViewModel;
    }

    public JFrame build() {
        JFrame application = new JFrame("Country Explorer");

        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        viewManagerModel.setState(loginSignUpView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}


