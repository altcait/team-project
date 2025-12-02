package app;

import data_access.ApiSearchByRegionDataAccessObject;
import data_access.ApiSearchByCurrencyDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.UserCSVDataAccess;

import entity.CountryFactory;
import entity.UserFactory;

import interface_adapter.ViewManagerModel;
import interface_adapter.retrieve_saved_lists.ViewSavedListsController;
import interface_adapter.retrieve_saved_lists.ViewSavedListsPresenter;
import interface_adapter.retrieve_saved_lists.ViewSavedListsViewModel;
import interface_adapter.view_selected_list.ViewSelectedListController;
import interface_adapter.view_selected_list.ViewSelectedListPresenter;
import interface_adapter.view_selected_list.ViewSelectedListViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.profile.*;
import interface_adapter.save_country.SaveCountryController;
import interface_adapter.save_country.SaveCountryPresenter;
import interface_adapter.save_country.SaveCountryViewModel;
import interface_adapter.search.byregion.SearchByRegionController;
import interface_adapter.search.byregion.SearchByRegionPresenter;
import interface_adapter.search.byregion.SearchByRegionViewModel;
import interface_adapter.signup.*;
import interface_adapter.search.bycurrency.*;

import use_case.RetrieveSavedLists.ViewSavedListsInputBoundary;
import use_case.RetrieveSavedLists.ViewSavedListsInteractor;
import use_case.RetrieveSavedLists.ViewSavedListsOutputBoundary;
import use_case.ViewSelectedList.ViewSelectedListInputBoundary;
import use_case.ViewSelectedList.ViewSelectedListInteractor;
import use_case.ViewSelectedList.ViewSelectedListOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserAccess;
import use_case.profile.*;
import use_case.save_country.SaveCountryInputBoundary;
import use_case.save_country.SaveCountryInteractor;
import use_case.save_country.SaveCountryOutputBoundary;
import use_case.search.byregion.SearchByRegionDataAccessInterface;
import use_case.search.byregion.SearchByRegionInputBoundary;
import use_case.search.byregion.SearchByRegionInteractor;
import use_case.search.byregion.SearchByRegionOutputBoundary;
import use_case.signup.*;
import use_case.search.bycurrency.*;

import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private ViewManager viewManager;

    // Search views and view models
    private SearchesView searchesView;
    private SearchByLanguageView searchByLanguageView;
    private SearchByRegionView searchByRegionView;
    private SearchByCurrencyView searchByCurrencyView;
    private SearchByRegionViewModel searchByRegionViewModel;
    private SearchByCurrencyViewModel searchByCurrencyViewModel;

    // App general views / view-models
    private LoginSignUpView loginSignUpView;
    private LoginViewModel loginViewModel;
    private SaveCountryView saveCountryView;
    private SaveCountryViewModel saveCountryViewModel;
    private SignUpViewModel signUpViewModel;

    private ProfilePresenter profilePresenter;
    private ProfileInteractor profileInteractor;

    // --- Data access objects (single instances) ---
    private final FileUserDataAccessObject fileUserDataAccessObject;
    private final UserCSVDataAccess userDataAccess;   // CSV DAO
    private final LoginUserAccess loginDataAccess;    // alias to same DAO

    // Views / view-models
    private SelectedListView selectedListView;
    private ViewSelectedListViewModel viewSelectedListViewModel;
    private ProfileViewModel profileViewModel;
    private EditProfileViewModel editProfileViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        // Shared DAOs
        userDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());
        loginDataAccess = userDataAccess; // same object, typed by interface
        fileUserDataAccessObject = new FileUserDataAccessObject("favouritesRepository.json");

        profileViewModel = new ProfileViewModel();
    }

    // ---------- Searches view ----------

    public AppBuilder addSearchesView() {
        searchesView = new SearchesView(viewManagerModel);
        cardPanel.add(searchesView, searchesView.getViewName());
        return this;
    }

    // ---------- Login & Sign-up ----------

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
        return this;
    }

    // ---------- Search by Currency ----------

    public AppBuilder addSearchByCurrencyView() {
        searchByCurrencyViewModel = new SearchByCurrencyViewModel();
        searchByCurrencyView = new SearchByCurrencyView(searchByCurrencyViewModel);
        cardPanel.add(searchByCurrencyView, searchByCurrencyView.getViewName());
        return this;
    }

    // ---------- Search by Region ----------

    public AppBuilder addSearchByRegionView() {
        searchByRegionViewModel = new SearchByRegionViewModel();
        searchByRegionView = new SearchByRegionView(searchByRegionViewModel);
        cardPanel.add(searchByRegionView, searchByRegionView.getViewName());
        return this;
    }

    public AppBuilder addSearchByCurrencyUseCase() {
        // Data access for search by currency
        CountryFactory countryFactory = new CountryFactory();
        SearchByCurrencyDataAccessInterface dataAccess =
                new ApiSearchByCurrencyDataAccessObject(countryFactory);

        // Presenter: wires navigation to SaveCountry + SelectedList
        SearchByCurrencyOutputBoundary presenter =
                new SearchByCurrencyPresenter(
                        searchByCurrencyViewModel,
                        viewManagerModel,
                        saveCountryViewModel,
                        viewSelectedListViewModel
                );

        // Interactor
        SearchByCurrencyInputBoundary interactor =
                new SearchByCurrencyInteractor(dataAccess, presenter);

        // Controller
        SearchByCurrencyController controller =
                new SearchByCurrencyController(interactor);

        // Connect controller to view
        searchByCurrencyView.setController(controller);

        return this;
    }

    public AppBuilder addSearchByRegionUseCase() {
        // Data access for search by region
        CountryFactory countryFactory = new CountryFactory();
        SearchByRegionDataAccessInterface dataAccess =
                new ApiSearchByRegionDataAccessObject(countryFactory);

        // Presenter: wires navigation to SaveCountry + SelectedList
        SearchByRegionOutputBoundary presenter =
                new SearchByRegionPresenter(
                        searchByRegionViewModel,
                        viewManagerModel,
                        saveCountryViewModel,
                        viewSelectedListViewModel
                );

        // Interactor
        SearchByRegionInputBoundary interactor =
                new SearchByRegionInteractor(dataAccess, presenter);

        // Controller
        SearchByRegionController controller =
                new SearchByRegionController(interactor);

        // Connect controller to view
        searchByRegionView.setController(controller);

        return this;
    }

    // ---------- Save Country ----------

    public AppBuilder addSaveCountryView() {
        saveCountryViewModel = new SaveCountryViewModel();
        saveCountryView = new SaveCountryView(saveCountryViewModel, viewManagerModel);
        cardPanel.add(saveCountryView, saveCountryView.getViewName());
        return this;
    }

    public AppBuilder addSaveCountryUseCase() {
        SaveCountryOutputBoundary saveCountryOutputBoundary =
                new SaveCountryPresenter(saveCountryViewModel);
        SaveCountryInputBoundary saveCountryInteractor =
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
        viewSelectedListViewModel = new ViewSelectedListViewModel();
        ViewSelectedListOutputBoundary selectedListPresenter =
                new ViewSelectedListPresenter(viewSelectedListViewModel);

        ViewSelectedListInputBoundary selectedListInteractor =
                new ViewSelectedListInteractor(selectedListPresenter, fileUserDataAccessObject);

        ViewSelectedListController selectedListController =
                new ViewSelectedListController(selectedListInteractor);

        selectedListView =
                new SelectedListView(
                        selectedListController,
                        viewSelectedListViewModel,
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
        ViewSavedListsViewModel listsViewModel = new ViewSavedListsViewModel();

        ViewSavedListsOutputBoundary listsPresenter =
                new ViewSavedListsPresenter(listsViewModel);

        ViewSavedListsInputBoundary listsInteractor =
                new ViewSavedListsInteractor(listsPresenter, fileUserDataAccessObject);

        ViewSavedListsController listsController =
                new ViewSavedListsController(listsInteractor);

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

    // ---------- Profile & Edit Profile ----------

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

    // ---------- Build frame ----------

    public JFrame build() {
        JFrame application = new JFrame("Country Explorer");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        // Start on login screen
        viewManagerModel.setState(loginSignUpView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}