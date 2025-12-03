package interface_adapter.search.byregion;

import interface_adapter.ViewManagerModel;
import interface_adapter.save_country.SaveCountryViewModel;
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

    /**
     * Full constructor used in the real app when navigation is needed.
     */
    public SearchByRegionPresenter(SearchByRegionViewModel viewModel,
                                   ViewManagerModel viewManagerModel,
                                   SaveCountryViewModel saveCountryViewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.saveCountryViewModel = saveCountryViewModel;
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
        viewManagerModel.setState(saveCountryViewModel.getViewName());
        // fire property change in view model to populate user's lists
        // needs to be done here rather than when building the view because there is no logged in user at that point
        saveCountryViewModel.firePropertyChange();
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
