package app;

import interface_adapter.ViewManagerModel;
import view.*;
import data_access.UserCSVDataAccess;
import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.RetrieveSavedLists.ViewSavedListsController;
import interface_adapter.RetrieveSavedLists.ViewSavedListsPresenter;
import interface_adapter.RetrieveSavedLists.ViewSavedListsViewModel;
import interface_adapter.ViewSelectedList.ViewSelectedListController;
import interface_adapter.ViewSelectedList.ViewSelectedListPresenter;
import interface_adapter.ViewSelectedList.ViewSelectedListViewModel;
import interface_adapter.save_country.SaveCountryViewModel;
import interface_adapter.save_country.SaveCountryController;
import interface_adapter.save_country.SaveCountryPresenter;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserAccess;
import use_case.RetrieveSavedLists.ViewSavedListsInputBoundary;
import use_case.RetrieveSavedLists.ViewSavedListsInteractor;
import use_case.RetrieveSavedLists.ViewSavedListsOutputBoundary;
import use_case.ViewSelectedList.ViewSelectedListInputBoundary;
import use_case.ViewSelectedList.ViewSelectedListInteractor;
import use_case.ViewSelectedList.ViewSelectedListOutputBoundary;
import view.LoginView;
import view.ListsView;
import view.SelectedListView;
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


    private SearchesView searchesView;
    private SearchByLanguageView searchByLanguageView;
    private SearchByRegionView searchByRegionView;
    private SearchByCurrencyView searchByCurrencyView;

    private LoginSignUpView loginSignUpView;
    private LoginViewModel loginViewModel;
    private SaveCountryView saveCountryView;
    private SaveCountryViewModel saveCountryViewModel;
    private SignUpViewModel signUpViewModel;
    final FileUserDataAccessObject fileUserDataAccessObject = new FileUserDataAccessObject("favouritesRepository.json");
    final LoginUserAccess loginDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());


    // NEW: we keep a reference so ListsView can call SelectedListView
    private SelectedListView selectedListView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSearchesView() {
        searchesView = new SearchesView(viewManagerModel);
        cardPanel.add(searchesView, searchesView.getViewName());
        return this;
    }

    // TODO placeholder: update when merged with Search by Language use case
    public AppBuilder addSearchByLanguageView() {
        searchByLanguageView = new SearchByLanguageView();
        cardPanel.add(searchByLanguageView, searchByLanguageView.getViewName());
    // ======================
    // Login feature wiring
    // ======================

    public AppBuilder addLoginView() {
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

    // TODO placeholder: update when merged with Search by Region use case
    public AppBuilder addSearchByRegionView() {
        searchByRegionView = new SearchByRegionView();
        cardPanel.add(searchByRegionView, searchByRegionView.getViewName());
        return this;
    }

    // TODO placeholder: update when merged with Search by Currency use case
    public AppBuilder addSearchByCurrencyView() {
        searchByCurrencyView = new SearchByCurrencyView();
        cardPanel.add(searchByCurrencyView, searchByCurrencyView.getViewName());
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

    // ============================
    // View saved lists (lists page)
    // ============================

    public AppBuilder addViewSavedLists() {
        // 1. Create the view model for the lists screen
        ViewSavedListsViewModel listsViewModel = new ViewSavedListsViewModel();

        // 2. Create the presenter
        ViewSavedListsOutputBoundary listsPresenter =
                new ViewSavedListsPresenter(listsViewModel);

        // 3. Create the interactor (use case)
        ViewSavedListsInputBoundary listsInteractor =
                new ViewSavedListsInteractor(listsPresenter);

        // 4. Create the controller
        ViewSavedListsController listsController =
                new ViewSavedListsController(listsInteractor);

        // 5. Create the view and add it to the card layout.
        //    Note: selectedListView must already have been created in addViewSelectedList().
        ListsView listsView =
                new ListsView(listsController, listsViewModel, selectedListView, viewManagerModel);
        cardPanel.add(listsView, listsView.viewName);

        return this;
    }

    // ==================================
    // View selected list (detail page)
    // ==================================

    public AppBuilder addViewSelectedList() {
        ViewSelectedListViewModel selectedListViewModel = new ViewSelectedListViewModel();
        ViewSelectedListOutputBoundary selectedListPresenter =
                new ViewSelectedListPresenter(selectedListViewModel);
        ViewSelectedListInputBoundary selectedListInteractor =
                new ViewSelectedListInteractor(selectedListPresenter);
        ViewSelectedListController selectedListController =
                new ViewSelectedListController(selectedListInteractor);

        // NOTE: viewManagerModel is passed in here
        selectedListView =
                new SelectedListView(selectedListController, selectedListViewModel, viewManagerModel);
        cardPanel.add(selectedListView, selectedListView.viewName);

        return this;
    }


    public JFrame build() {
        JFrame application = new JFrame("Searches View");

        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        viewManagerModel.setState(searchesView.getViewName());
        // Start on the lists screen so you can test:
        // 1) Load My Lists
        // 2) Click Example List -> goes to SelectedListView
        viewManagerModel.setState("lists");
        viewManagerModel.setState(loginSignUpView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
