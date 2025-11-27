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
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserAccess;
import use_case.RetrieveSavedLists.ViewSavedListsInputBoundary;
import use_case.RetrieveSavedLists.ViewSavedListsInteractor;
import use_case.RetrieveSavedLists.ViewSavedListsOutputBoundary;
import view.LoginView;
import view.ListsView;
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
        LoginInputBoundary interactor =
                new LoginInteractor(dataAccess, presenter);

        LoginController controller = new LoginController(interactor);
        loginView.setLoginController(controller);

        return this;
    }

    // ============================
    // View saved lists wiring
    // ============================

    /**
     * Set up the "view saved lists" feature (Derek's use case).
     * This registers the ListsView screen and wires its use case.
     */
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

        // 5. Create the view and add it to the card layout
        ListsView listsView = new ListsView(listsController, listsViewModel);
        cardPanel.add(listsView, listsView.viewName);

        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("Example App");

        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        // By default start on the login view.
        // To test the lists view directly, you can temporarily change this to:
        //   viewManagerModel.setState("lists");
        viewManagerModel.setState("lists");
        viewManagerModel.firePropertyChange();

        return application;
    }
}
