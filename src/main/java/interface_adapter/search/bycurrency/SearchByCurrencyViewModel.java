package interface_adapter.search.bycurrency;

import interface_adapter.ViewModel;

/**
 * The View Model for the Search by Currency view.
 */
public class SearchByCurrencyViewModel extends ViewModel<SearchByCurrencyState> {

    public static final String VIEW_NAME = "search by currency";

    public SearchByCurrencyViewModel() {
        super(VIEW_NAME);
        setState(new SearchByCurrencyState());
    }
}
