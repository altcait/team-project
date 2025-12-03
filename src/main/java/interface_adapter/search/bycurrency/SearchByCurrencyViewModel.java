package interface_adapter.search.bycurrency;

import interface_adapter.ViewModel;
import interface_adapter.search.bycurrency.SearchByCurrencyState;

public class SearchByCurrencyViewModel extends ViewModel<SearchByCurrencyState>{

    public static final String VIEW_NAME = "SearchByCurrency";

    public SearchByCurrencyViewModel() {
        super(VIEW_NAME);
        setState(new SearchByCurrencyState());
    }

}



