package interface_adapter.search.ByLanguage;

import interface_adapter.ViewManagerModel;
import view.SaveCountryView;
import use_case.search.by_language.SearchByLanguageOutputBoundary;
import use_case.search.by_language.SearchByLanguageOutputData;
import view.SelectedListView;

/**
 * Presenter for the Search by Language use case.
 */
public class SearchByLanguagePresenter implements SearchByLanguageOutputBoundary {

    private final SearchByLanguageViewModel searchByLanguageViewModel;
    private final ViewManagerModel viewManagerModel;

    private final SelectedListView selectedListView;
    private final SaveCountryView saveCountryView;

    public SearchByLanguagePresenter(SearchByLanguageViewModel searchByLanguageViewModel,
                                     ViewManagerModel viewManagerModel,
                                     SelectedListView selectedListView,
                                     SaveCountryView saveCountryView) {
        this.searchByLanguageViewModel = searchByLanguageViewModel;
        this.viewManagerModel = viewManagerModel;
        this.selectedListView = selectedListView;
        this.saveCountryView = saveCountryView;
    }

    @Override
    public void presentCountries(SearchByLanguageOutputData outputData) {
        SearchByLanguageState state = searchByLanguageViewModel.getState();
        state.setCountries(outputData.getCountries());
        state.setSelectedLanguage(outputData.getSelectedLanguage());
        state.setErrorMessage(null);

        searchByLanguageViewModel.firePropertyChange();
    }

    public void presentLanguages(SearchByLanguageOutputData outputData) {
        // TODO
        SearchByLanguageState state = searchByLanguageViewModel.getState();
        state.setErrorMessage(null);

        state.setLanguageOptions(outputData.getLanguageOptions());

        searchByLanguageViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SearchByLanguageState state = searchByLanguageViewModel.getState();
        state.setErrorMessage(errorMessage);
//        state.setCountries(List.of());    // TODO: for what?
        searchByLanguageViewModel.firePropertyChange();
    }

    // TODO: back" from Search view(s)
    @Override
    public void switchToPreviousView() {
        viewManagerModel.setState(selectedListView.viewName);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToSaveCountryView() {
        viewManagerModel.setState(saveCountryView.getViewName());
        viewManagerModel.firePropertyChange();
    }
}