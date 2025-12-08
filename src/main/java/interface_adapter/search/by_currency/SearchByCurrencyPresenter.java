package interface_adapter.search.by_currency;

import interface_adapter.ViewManagerModel;
import interface_adapter.save_country.SaveCountryViewModel;
import interface_adapter.view_selected_list.ViewSelectedListViewModel;


public class SearchByCurrencyPresenter implements use_case.search.by_currency.SearchByCurrencyOutputBoundary {

    private final SearchByCurrencyViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final SaveCountryViewModel saveCountryViewModel;
    private final ViewSelectedListViewModel viewSelectedListViewModel;

    /**
     * Demo / simple constructor: only updates this view's ViewModel,
     * no navigation (no ViewManagerModel injected).
     */
    public SearchByCurrencyPresenter(SearchByCurrencyViewModel viewModel) {
        this(viewModel, null, null, null);
    }

    /**
     * Full constructor used in the real app when navigation is needed.
     */
    public SearchByCurrencyPresenter(SearchByCurrencyViewModel viewModel,
                                   ViewManagerModel viewManagerModel,
                                   SaveCountryViewModel saveCountryViewModel,
                                   ViewSelectedListViewModel viewSelectedListViewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.saveCountryViewModel = saveCountryViewModel;
        this.viewSelectedListViewModel = viewSelectedListViewModel;
    }

    @Override
    public void presentCountries(use_case.search.by_currency.SearchByCurrencyOutputData outputData) {
        SearchByCurrencyState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setSelectedCurrency(outputData.getCurrency());
        state.setCountries(outputData.getCountries());

        viewModel.firePropertyChange();
    }


    @Override
    public void presentCurrencies(use_case.search.by_currency.SearchByCurrencyOutputData outputData) {
        SearchByCurrencyState state = viewModel.getState();
        state.setErrorMessage(null);

        state.setCurrencyOptions(outputData.getCurrencies());

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
