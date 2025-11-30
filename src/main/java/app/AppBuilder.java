package app;

import interface_adapter.ViewManagerModel;
import view.*;
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

    public JFrame build() {
        JFrame application = new JFrame("Searches View");

        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        viewManagerModel.setState(searchesView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}

