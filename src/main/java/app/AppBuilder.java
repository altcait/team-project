package app;

import interface_adapter.ViewManagerModel;
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

    private SearchByLanguageView searchByLanguageView;
    private SearchByLanguageViewModel searchByLanguageViewModel;
    SearchByLanguageCountryDataAccessInterface searchByLanguageCountryDataAccessInterface;
    private SearchByLanguagePresenter searchByLanguagePresenter;

    private ApiSearchByRegionDataAccessObject countryDataAccessObject;  // TODO: pull updated DAO

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSearchByLanguageView() {
        searchByLanguageViewModel = new SearchByLanguageViewModel();
        searchByLanguageView = new SearchByLanguageView(searchByLanguageViewModel);
        cardPanel.add(searchByLanguageView, searchByLanguageView.getViewName());
        return this;
    }

    public AppBuilder addSearchByLanguageUseCase() {
        final SearchByLanguageOutputBoundary searchByLanguageOutputBoundary = new SearchByLanguagePresenter(
                searchByLanguageViewModel, viewManagerModel);
        final SearchByLanguageInputBoundary searchByLanguageInteractor = new SearchByLanguageInteractor(
                countryDataAccessObject, searchByLanguagePresenter);

        SearchByLanguageController searchByLanguageController = new SearchByLanguageController(searchByLanguageInteractor);
        searchByLanguageView.setSearchByLanguageController(searchByLanguageController);
        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("Example App");

        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

//        viewManagerModel.setState(loginView.getViewName()); // TODO: pull login use case code
        viewManagerModel.firePropertyChange();

        return application;
    }
}
