package interface_adapter.search.byregion;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
import interface_adapter.save_country.SaveCountryViewModel;
import interface_adapter.ViewSelectedList.ViewSelectedListViewModel;
import use_case.search.byregion.SearchByRegionOutputBoundary;
import use_case.search.byregion.SearchByRegionOutputData;

/**
 * Presenter for the Search By Region use case.
 * Maps output data to the view model state and handles navigation.
 */
public class SearchByRegionPresenter implements SearchByRegionOutputBoundary {

    private final SearchByRegionViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final SaveCountryViewModel saveCountryViewModel;
    private final ViewSelectedListViewModel viewSelectedListViewModel;

    /**
     * Demo / simple constructor: only updates this view's ViewModel,
     * no navigation (no ViewManagerModel injected).
     */
    public SearchByRegionPresenter(SearchByRegionViewModel viewModel) {
        this(viewModel, null, null, null);
    }

    /**
     * Full constructor used in the real app when navigation is needed.
     */
    public SearchByRegionPresenter(SearchByRegionViewModel viewModel,
                                   ViewManagerModel viewManagerModel,
                                   SaveCountryViewModel saveCountryViewModel,
                                   ViewSelectedListViewModel viewSelectedListViewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.saveCountryViewModel = saveCountryViewModel;
        this.viewSelectedListViewModel = viewSelectedListViewModel;
    }

    @Override
    public void presentCountries(SearchByRegionOutputData outputData) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setSelectedRegion(outputData.getRegion());
        state.setSelectedSubregion(outputData.getSubregion());
        state.setCountries(outputData.getCountries());
        // not change region/subregion options here!

        viewModel.firePropertyChange();
    }

    @Override
    public void presentSubregions(SearchByRegionOutputData outputData) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setSelectedRegion(outputData.getRegion());
        // Clear current subregion selection when region changes
        state.setSelectedSubregion(null);
        state.setSubregionOptions(outputData.getSubregions());

        viewModel.firePropertyChange();
    }

    @Override
    public void presentRegions(SearchByRegionOutputData outputData) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setRegionOptions(outputData.getRegions());
        // Only fill region list initially

        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }

    @Override
    public void switchToSaveCountryView() {
        if (viewManagerModel == null || saveCountryViewModel == null) {
            return;
        }
        viewManagerModel.setState("save country");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToSelectedListView() {
        if (viewManagerModel == null) {
            return;
        }
        // The view name of SelectedListView in its code is selected_list
        viewManagerModel.setState("selected_list");
        viewManagerModel.firePropertyChange();
    }
}