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
import interface_adapter.profile.*;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserAccess;
import use_case.profile.*;

import use_case.RetrieveSavedLists.ViewSavedListsInputBoundary;
import use_case.RetrieveSavedLists.ViewSavedListsInteractor;
import use_case.RetrieveSavedLists.ViewSavedListsOutputBoundary;
import use_case.ViewSelectedList.ViewSelectedListInputBoundary;
import use_case.ViewSelectedList.ViewSelectedListInteractor;
import use_case.ViewSelectedList.ViewSelectedListOutputBoundary;
import view.*;
import use_case.save_country.SaveCountryInputBoundary;
import use_case.save_country.SaveCountryOutputBoundary;
import use_case.save_country.SaveCountryInteractor;

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
    private ProfilePresenter profilePresenter;
    private ProfileInteractor profileInteractor;

    final FileUserDataAccessObject fileUserDataAccessObject = new FileUserDataAccessObject("favouritesRepository.json");
    final LoginUserAccess loginDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());

    // --- Data access objects ---
    private final FileUserDataAccessObject fileUserDataAccessObject;
    private final UserCSVDataAccess userDataAccess;   // single CSV DAO
    private final LoginUserAccess loginDataAccess;    // alias to same DAO

    // For views
    private SelectedListView selectedListView;
    private ProfileViewModel profileViewModel;
    private EditProfileViewModel editProfileViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        // one shared CSV DAO
        userDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());
        profileViewModel = new ProfileViewModel();
    }

    public AppBuilder addLoginSignUpView() {
        loginViewModel = new LoginViewModel();
        loginSignUpView = new LoginSignUpView(loginViewModel);

        cardPanel.add(loginSignUpView, loginSignUpView.getViewName());
        return this;
    }

    public AppBuilder addLoginUseCase() {

        LoginOutputBoundary loginPresenter = new LoginPresenter(
                loginViewModel,
                viewManagerModel,
                profileViewModel,
                userDataAccess
        );

        LoginInputBoundary loginInteractor =
                new LoginInteractor(userDataAccess, loginPresenter);

        LoginController loginController = new LoginController(loginInteractor);
        loginSignUpView.setLoginController(loginController);

        return this;
    }


    public AppBuilder addSignUpUseCase() {
        signUpViewModel = new SignUpViewModel();

        SignUpUserAccess signupDataAccess = userDataAccess;

        SignUpOutputBoundary signupPresenter =
                new SignUpPresenter(viewManagerModel, signUpViewModel, loginViewModel);
        SignUpInputBoundary signupInteractor =
                new SignUpInteractor(signupDataAccess, signupPresenter, new UserFactory());

        SignUpController signupController = new SignUpController(signupInteractor);
        loginSignUpView.setSignupController(signupController);

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
        final SaveCountryOutputBoundary saveCountryOutputBoundary =
                new SaveCountryPresenter(saveCountryViewModel);
        final SaveCountryInputBoundary saveCountryInteractor =
                new SaveCountryInteractor(
                        loginDataAccess,
                        fileUserDataAccessObject,
                        saveCountryOutputBoundary
                );

        SaveCountryController saveCountryController =
                new SaveCountryController(saveCountryInteractor);
        saveCountryView.setSaveCountryController(saveCountryController);
        return this;
    }

    // ---------- Selected list (detail) ----------
    public AppBuilder addViewSelectedList() {
        ViewSelectedListViewModel selectedListViewModel = new ViewSelectedListViewModel();
        ViewSelectedListOutputBoundary selectedListPresenter =
                new ViewSelectedListPresenter(selectedListViewModel);

        ViewSelectedListInputBoundary selectedListInteractor =
                new ViewSelectedListInteractor(selectedListPresenter, fileUserDataAccessObject);

        ViewSelectedListController selectedListController =
                new ViewSelectedListController(selectedListInteractor);

        selectedListView =
                new SelectedListView(
                        selectedListController,
                        selectedListViewModel,
                        viewManagerModel,
                        () -> {
                            String current = userDataAccess.getCurrentUsername();
                            return current != null ? current : "";
                        },
                        fileUserDataAccessObject
                );

        cardPanel.add(selectedListView, selectedListView.viewName);

        return this;
    }


    // ---------- Saved lists (overview) ----------
    public AppBuilder addViewSavedLists() {
        // 1. Create the view model for the lists screen
        ViewSavedListsViewModel listsViewModel = new ViewSavedListsViewModel();

        // 2. Create the presenter
        ViewSavedListsOutputBoundary listsPresenter =
                new ViewSavedListsPresenter(listsViewModel);

        // 3. Create the interactor (use case)
        ViewSavedListsInputBoundary listsInteractor =
                new ViewSavedListsInteractor(listsPresenter, fileUserDataAccessObject);

        // 4. Create the controller
        ViewSavedListsController listsController =
                new ViewSavedListsController(listsInteractor);

        // 5. Create the view and add it to the card layout.
        //    We pass:
        //    - controller + viewModel
        //    - selectedListView (detail view)
        //    - viewManagerModel (for navigation)
        //    - a Supplier<String> that returns the currently logged-in username
        //    - fileUserDataAccessObject so Create List can write to JSON
        ListsView listsView =
                new ListsView(
                        listsController,
                        listsViewModel,
                        selectedListView,
                        viewManagerModel,
                        () -> {
                            String current = loginDataAccess.getCurrentUsername();
                            return current != null ? current : "";
                        },
                        fileUserDataAccessObject
                );

        cardPanel.add(listsView, listsView.viewName);

        return this;
    }

    public AppBuilder addProfileUseCase() {

        profilePresenter =
                new ProfilePresenter(viewManagerModel, loginViewModel, profileViewModel);

        profileInteractor =
                new ProfileInteractor(profilePresenter, userDataAccess);

        ProfileController profileController =
                new ProfileController(profileInteractor);

        ProfileView profileView = new ProfileView(profileViewModel);
        profileView.setController(profileController);

        cardPanel.add(profileView, profileView.getViewName());
        return this;
    }


    public AppBuilder addEditProfileUseCase() {
        editProfileViewModel = new EditProfileViewModel();
        EditProfileView editProfileView = new EditProfileView(editProfileViewModel);

        ProfileController profileController = new ProfileController(profileInteractor);

        editProfileView.setController(profileController);
        cardPanel.add(editProfileView, editProfileView.getViewName());
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
