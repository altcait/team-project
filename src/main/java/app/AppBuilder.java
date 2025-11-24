package app;

import javax.swing.*;

import data_access.FileUserDataAccessObject;
import interface_adapter.save_country.SaveCountryViewModel;
import interface_adapter.save_country.SaveCountryController;
import interface_adapter.save_country.SaveCountryPresenter;
import use_case.save_country.SaveCountryInputBoundary;
import use_case.save_country.SaveCountryOutputBoundary;
import use_case.save_country.SaveCountryInteractor;

import view.SaveCountryView;

import java.awt.*;

public class AppBuilder {
    private final JPanel viewPanel = new JPanel();

    final FileUserDataAccessObject fileUserDataAccessObject = new FileUserDataAccessObject("favouritesRepository.json");

    private SaveCountryView saveCountryView;
    private SaveCountryViewModel saveCountryViewModel;

    public AppBuilder() {
        viewPanel.setLayout(new CardLayout());
    }

    public AppBuilder addSaveCountryView() {
        saveCountryViewModel = new SaveCountryViewModel();
        saveCountryView = new SaveCountryView(saveCountryViewModel);
        viewPanel.add(saveCountryView);
        return this;
    }

    public AppBuilder addSaveCountryUseCase() {
        final SaveCountryOutputBoundary saveCountryOutputBoundary = new SaveCountryPresenter(saveCountryViewModel);
        final SaveCountryInputBoundary saveCountryInteractor = new SaveCountryInteractor(
                fileUserDataAccessObject,
                saveCountryOutputBoundary
        );

        SaveCountryController saveCountryController = new SaveCountryController(saveCountryInteractor);
        saveCountryView.setSaveCountryController(saveCountryController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Country Explorer");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(800, 600);

        application.add(viewPanel);

        return application;
    }
}
