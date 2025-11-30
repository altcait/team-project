package interface_adapter.search.ByLanguage;

import interface_adapter.ViewManagerModel;
import use_case.search.ByLanguage.SearchByLanguageOutputBoundary;
import use_case.search.ByLanguage.SearchByLanguageOutputData;

/**
 * Presenter for the Search by Language use case.
 */
public class SearchByLanguagePresenter implements SearchByLanguageOutputBoundary {

    private final SearchByLanguageViewModel searchByLanguageViewModel;
    private final ViewManagerModel viewManagerModel;

    private final SearchesView searchesView;    // TODO: update to appropriate "previous view" ViewModel

    public SearchByLanguagePresenter(SearchByLanguageViewModel searchByLanguageViewModel,
                                     ViewManagerModel viewManagerModel) {
        this.searchByLanguageViewModel = searchByLanguageViewModel;
        this.viewManagerModel = viewManagerModel;
        //this.profileViewModel = profileViewModel; // TODO: update to appropriate "previous view" ViewModel
    }

    @Override
    public void presentCountries(SearchByLanguageOutputData outputData) {
        // TODO
//        SearchByLanguageState state = searchByLanguageViewModel.getState();
//        state.setErrorMessage(null);
//
//        state.setSelectedRegion(outputData.getRegion());
//        state.setSelectedSubregion(outputData.getSubregion());
//        state.setCountries(outputData.getCountries());
//        // Do not change regions/subregions and keep the previous option
//
//        searchByLanguageViewModel.firePropertyChanged();
    }

    public void presentLanguages(SearchByLanguageOutputData outputData) {
        // TODO
//        SearchByLanguageState state = searchByLanguageViewModel.getState();
//        state.setErrorMessage(null);
//
//        state.setRegionOptions(outputData.getRegions());
//        // only fill in the region list initially
//
//        searchByLanguageViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // TODO
//        SearchByLanguageState state = searchByLanguageViewModel.getState();
//        state.setErrorMessage(errorMessage);
//        searchByLanguageViewModel.firePropertyChanged();
    }

    // TODO: back" from Search view(s)
    @Override
    public void switchToPreviousView() {
        viewManagerModel.setState(searchesView.getViewName());
        viewManagerModel.firePropertyChange();
    }
}