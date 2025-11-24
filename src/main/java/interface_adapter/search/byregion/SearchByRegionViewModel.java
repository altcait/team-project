package interface_adapter.search.byregion;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for the Search by Region view.
 * Holds a SearchByRegionState and notifies listeners when it changes.
 */
public class SearchByRegionViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SearchByRegionState state = new SearchByRegionState();

    public SearchByRegionState getState() {
        return state;
    }

    public void setState(SearchByRegionState state) {
        this.state = state;
        support.firePropertyChange("state", null, this.state);
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
