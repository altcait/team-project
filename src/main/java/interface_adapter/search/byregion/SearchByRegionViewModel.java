package interface_adapter.search.byregion;

import interface_adapter.ViewModel;

/**
 * The View Model for the Search by Region View.
 */
public class SearchByRegionViewModel extends ViewModel<SearchByRegionState> {

    public static final String VIEW_NAME = "search by region";

    public SearchByRegionViewModel() {
        super(VIEW_NAME);
        setState(new SearchByRegionState());
    }
}