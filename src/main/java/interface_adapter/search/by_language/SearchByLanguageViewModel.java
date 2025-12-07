package interface_adapter.search.by_language;

import interface_adapter.ViewModel;

/**
 * The view model for the Search by Language view.
 * Holds a SearchByLanguageState and notifies listeners when it changes.
 */
public class SearchByLanguageViewModel extends ViewModel<SearchByLanguageState> {

    public static final String TITLE_LABEL = "Search by Language View";
    public static final String LANGUAGE_LABEL = "Select Language:";

    public static final String SEARCH_BUTTON_LABEL = "Search";
    public static final String SAVE_BUTTON_LABEL = "Save Country";
    public static final String BACK_BUTTON_LABEL = "Back";

    public SearchByLanguageViewModel () {
        super("searchByLanguage");
        setState(new SearchByLanguageState());
    }

    // TODO: remove?
//    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
//    private SearchByLanguageState state = new SearchByLanguageState();

//    public void setState(SearchByLanguageState state) {
//        this.state = state;
//        support.firePropertyChange("state", null, this.state);
//    }

//    public void firePropertyChanged() {
//        support.firePropertyChange("state", null, this.state);
//    }
}