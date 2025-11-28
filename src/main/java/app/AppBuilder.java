package app;

import data_access.UserCSVDataAccess;
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

    // NEW: we keep a reference so ListsView can call SelectedListView
    private SelectedListView selectedListView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // ======================
    // Login feature wiring
    // ======================

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
        JFrame application = new JFrame("Example App");

        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        // Start on the lists screen so you can test:
        // 1) Load My Lists
        // 2) Click Example List -> goes to SelectedListView
        viewManagerModel.setState("lists");
        viewManagerModel.firePropertyChange();

        return application;
    }
}
