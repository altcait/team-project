package app;

import data_access.UserCSVDataAccess;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserAccess;
import view.LoginSignUpView;
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

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginSignUpView() {
        loginViewModel = new LoginViewModel();
        loginSignUpView = new LoginSignUpView(loginViewModel);

        cardPanel.add(loginSignUpView, loginSignUpView.getViewName());
        return this;
    }

    public AppBuilder addLoginUseCase() {
        LoginUserAccess loginDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());

        LoginOutputBoundary loginPresenter = new LoginPresenter(loginViewModel);
        LoginInputBoundary loginInteractor = new LoginInteractor(loginDataAccess, loginPresenter);

        LoginController loginController = new LoginController(loginInteractor);
        loginSignUpView.setLoginController(loginController);

        return this;
    }

    public AppBuilder addSignUpUseCase() {
        signUpViewModel = new SignUpViewModel();
        SignUpUserAccess signupDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());

        SignUpOutputBoundary signupPresenter = new SignUpPresenter(viewManagerModel, signUpViewModel, loginViewModel);
        SignUpInputBoundary signupInteractor =
                new SignUpInteractor(signupDataAccess, signupPresenter, new UserFactory());

        SignUpController signupController = new SignUpController(signupInteractor);
        loginSignUpView.setSignupController(signupController);

        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("Example App");

        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        viewManagerModel.setState(loginSignUpView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}

