package interface_adapter.search.byregion;

import use_case.search.byregion.SearchByRegionOutputBoundary;
import use_case.search.byregion.SearchByRegionOutputData;

/**
 * Presenter for the Search by Region use case.
 * Maps output data to the view model state.
 */
public class SearchByRegionPresenter implements SearchByRegionOutputBoundary {

    private final SearchByRegionViewModel viewModel;

    public SearchByRegionPresenter(SearchByRegionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentCountries(SearchByRegionOutputData outputData) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setSelectedRegion(outputData.getRegion());
        state.setSelectedSubregion(outputData.getSubregion());
        state.setCountries(outputData.getCountries());
        // Do not change regions/subregions and keep the previous option

        viewModel.firePropertyChanged();
    }

    @Override
    public void presentSubregions(SearchByRegionOutputData outputData) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setSelectedRegion(outputData.getRegion());
        // Clear the current subregion selection when changing regions
        state.setSelectedSubregion(null);
        state.setSubregionOptions(outputData.getSubregions());

        viewModel.firePropertyChanged();
    }

    @Override
    public void presentRegions(SearchByRegionOutputData outputData) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setRegionOptions(outputData.getRegions());
        // only fill in the region list initially

        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SearchByRegionState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChanged();
    }
}