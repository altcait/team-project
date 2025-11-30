package interface_adapter.search.bycurrency;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
import use_case.search.bycurrency.SearchByCurrencyOutputBoundary;
import use_case.search.bycurrency.SearchByCurrencyOutputData;

/**
 * Presenter for the Search By Currency use case.
 * Maps output data from the interactor to the view model state.
 */
public class SearchByCurrencyPresenter implements SearchByCurrencyOutputBoundary {

    private final SearchByCurrencyViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final ViewModel<?> saveCountryViewModel;
    private final ViewModel<?> profileViewModel;

    public SearchByCurrencyPresenter(SearchByCurrencyViewModel viewModel) {
        this(viewModel, null, null, null);
    }

    public SearchByCurrencyPresenter(SearchByCurrencyViewModel viewModel,
                                     ViewManagerModel viewManagerModel,
                                     ViewModel<?> saveCountryViewModel,
                                     ViewModel<?> profileViewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.saveCountryViewModel = saveCountryViewModel;
        this.profileViewModel = profileViewModel;
    }

    @Override
    public void presentCountries(SearchByCurrencyOutputData outputData) {
        SearchByCurrencyState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setSelectedCurrency(outputData.getCurrency());
        state.setCountries(outputData.getCountries());

        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SearchByCurrencyState state = viewModel.getState();
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
