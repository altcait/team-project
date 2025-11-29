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
import view.LoginView;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.ProfileViewModel;
import interface_adapter.search.ByLanguage.SearchByLanguageController;
import interface_adapter.search.ByLanguage.SearchByLanguagePresenter;
import interface_adapter.search.ByLanguage.SearchByLanguageViewModel;
import use_case.search.ByLanguage.SearchByLanguageCountryDataAccessInterface;
import use_case.search.ByLanguage.SearchByLanguageInputBoundary;
import use_case.search.ByLanguage.SearchByLanguageInteractor;
import use_case.search.ByLanguage.SearchByLanguageOutputBoundary;
import view.SearchByLanguageView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private ViewManager viewManager;

    private LoginView loginView;
    private LoginViewModel loginViewModel;
    private SearchByLanguageView searchByLanguageView;
    private SearchByLanguageViewModel searchByLanguageViewModel;
    SearchByLanguageCountryDataAccessInterface searchByLanguageCountryDataAccessInterface;
    private SearchByLanguagePresenter searchByLanguagePresenter;

    private ProfileViewModel profileViewModel;  // TODO: update to appropriate "previous view" ViewModel

    private ApiSearchByRegionDataAccessObject countryDataAccessObject;  // TODO: pull updated DAO

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);

        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoginUseCase() {
        LoginUserAccess dataAccess = new UserCSVDataAccess("users.csv", new UserFactory());

        LoginOutputBoundary presenter = new LoginPresenter(loginViewModel);
        LoginInputBoundary interactor = new LoginInteractor(dataAccess, presenter);

        LoginController controller = new LoginController(interactor);
        loginView.setLoginController(controller);

    public AppBuilder addSearchByLanguageView() {
        searchByLanguageViewModel = new SearchByLanguageViewModel();
        searchByLanguageView = new SearchByLanguageView(searchByLanguageViewModel);
        cardPanel.add(searchByLanguageView, searchByLanguageView.getViewName());
        return this;
    }

    public AppBuilder addSearchByLanguageUseCase() {
        final SearchByLanguageOutputBoundary searchByLanguageOutputBoundary = new SearchByLanguagePresenter(
                searchByLanguageViewModel, viewManagerModel, profileViewModel);
        final SearchByLanguageInputBoundary searchByLanguageInteractor = new SearchByLanguageInteractor(
                countryDataAccessObject, searchByLanguagePresenter);

        SearchByLanguageController searchByLanguageController = new SearchByLanguageController(searchByLanguageInteractor);
        searchByLanguageView.setSearchByLanguageController(searchByLanguageController);
        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("Country Exploration App");

        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

//        viewManagerModel.setState(loginView.getViewName()); // TODO: pull login use case code
        viewManagerModel.firePropertyChange();

        return application;
    }
}

