package interface_adapter.search.byregion;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
import use_case.search.byregion.SearchByRegionOutputBoundary;
import use_case.search.byregion.SearchByRegionOutputData;

/**
 * Presenter for the Search By Region use case.
 * Maps output data to the view model state.
 */
public class SearchByRegionPresenter implements SearchByRegionOutputBoundary {

    private final SearchByRegionViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final ViewModel<?> saveCountryViewModel;
    private final ViewModel<?> profileViewModel;

    public SearchByRegionPresenter(SearchByRegionViewModel viewModel) {
        this(viewModel, null, null, null);
    }

    public SearchByRegionPresenter(SearchByRegionViewModel viewModel,
                                   ViewManagerModel viewManagerModel,
                                   ViewModel<?> saveCountryViewModel,
                                   ViewModel<?> profileViewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.saveCountryViewModel = saveCountryViewModel;
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void presentCountries(SearchByRegionOutputData outputData) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setSelectedRegion(outputData.getRegion());
        state.setSelectedSubregion(outputData.getSubregion());
        state.setCountries(outputData.getCountries());
        // Do not change regions/subregions and keep the previous option

        viewModel.firePropertyChange();
    }

    @Override
    public void presentSubregions(SearchByRegionOutputData outputData) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setSelectedRegion(outputData.getRegion());
        // Clear the current subregion selection when changing regions
        state.setSelectedSubregion(null);
        state.setSubregionOptions(outputData.getSubregions());

        viewModel.firePropertyChange();
    }

    @Override
    public void presentRegions(SearchByRegionOutputData outputData) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setRegionOptions(outputData.getRegions());
        // only fill in the region list initially

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
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToProfileView() {
        if (viewManagerModel == null || profileViewModel == null) {
            return;
        }
        viewManagerModel.setState(profileViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}