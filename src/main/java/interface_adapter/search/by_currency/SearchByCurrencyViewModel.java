package interface_adapter.search.by_currency;

import interface_adapter.ViewModel;

public class SearchByCurrencyViewModel extends ViewModel<SearchByCurrencyState>{

    public static final String VIEW_NAME = "SearchByCurrency";

    public SearchByCurrencyViewModel() {
        super(VIEW_NAME);
        setState(new SearchByCurrencyState());
    }

}



