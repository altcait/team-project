package app;

import data_access.ApiSearchByRegionDataAccessObject;
import entity.CountryFactory;
import interface_adapter.search.byregion.SearchByRegionController;
import interface_adapter.search.byregion.SearchByRegionPresenter;
import interface_adapter.search.byregion.SearchByRegionViewModel;
import use_case.search.byregion.SearchByRegionDataAccessInterface;
import use_case.search.byregion.SearchByRegionInputBoundary;
import use_case.search.byregion.SearchByRegionInteractor;
import use_case.search.byregion.SearchByRegionOutputBoundary;
import view.SearchByRegionView;

import javax.swing.*;

/**
 * Standalone demo for the Search By Region screen.
 */
public class SearchByRegionDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // 1. ViewModel
            SearchByRegionViewModel viewModel = new SearchByRegionViewModel();

            // 2. Presenter (demo version: only uses SearchByRegionViewModel)
            SearchByRegionOutputBoundary presenter =
                    new SearchByRegionPresenter(viewModel);

            // 3. Data access (with CountryFactory)
            CountryFactory countryFactory = new CountryFactory();
            SearchByRegionDataAccessInterface dataAccess =
                    new ApiSearchByRegionDataAccessObject(countryFactory);

            // 4. Interactor
            SearchByRegionInputBoundary interactor =
                    new SearchByRegionInteractor(dataAccess, presenter);

            // 5. Controller
            SearchByRegionController controller =
                    new SearchByRegionController(interactor);

            // 6. View (now only takes ViewModel, controller set separately)
            SearchByRegionView searchByRegionView =
                    new SearchByRegionView(viewModel);
            searchByRegionView.setController(controller);

            // 7. Frame
            JFrame frame = new JFrame("Travel Planner - Search By Region (Demo)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(searchByRegionView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}