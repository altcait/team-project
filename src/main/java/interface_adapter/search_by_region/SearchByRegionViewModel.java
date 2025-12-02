package interface_adapter.search_by_region;

import interface_adapter.ViewModel;

/**
 * The View Model for the Search by Region View.
 */
public class SearchByRegionViewModel extends ViewModel<SearchByRegionState> {

    public static final String VIEW_NAME = "SearchByRegion";

    public SearchByRegionViewModel() {
        super(VIEW_NAME);
        setState(new SearchByRegionState());
    }
}