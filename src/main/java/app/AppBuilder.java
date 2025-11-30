package app;

import data_access.UserCSVDataAccess;
import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.save_country.SaveCountryViewModel;
import interface_adapter.save_country.SaveCountryController;
import interface_adapter.save_country.SaveCountryPresenter;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserAccess;
import use_case.save_country.SaveCountryInputBoundary;
import use_case.save_country.SaveCountryOutputBoundary;
import use_case.save_country.SaveCountryInteractor;
import view.LoginSignUpView;
import view.ViewManager;
import view.SaveCountryView;
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
    private SaveCountryView saveCountryView;
    private SaveCountryViewModel saveCountryViewModel;
    private SignUpViewModel signUpViewModel;
    final FileUserDataAccessObject fileUserDataAccessObject = new FileUserDataAccessObject("favouritesRepository.json");
    final LoginUserAccess loginDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());


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
//        LoginUserAccess dataAccess = new UserCSVDataAccess("users.csv", new UserFactory());

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

    public AppBuilder addSaveCountryView() {
        saveCountryViewModel = new SaveCountryViewModel();
        saveCountryView = new SaveCountryView(saveCountryViewModel);
        cardPanel.add(saveCountryView);
        return this;
    }

    public AppBuilder addSaveCountryUseCase() {
        final SaveCountryOutputBoundary saveCountryOutputBoundary = new SaveCountryPresenter(saveCountryViewModel);
        final SaveCountryInputBoundary saveCountryInteractor = new SaveCountryInteractor(
                loginDataAccess,
                fileUserDataAccessObject,
                saveCountryOutputBoundary
        );

        SaveCountryController saveCountryController = new SaveCountryController(saveCountryInteractor);
        saveCountryView.setSaveCountryController(saveCountryController);
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

