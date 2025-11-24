package interface_adapter.save_country;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Save Country View.
 */
public class SaveCountryViewModel extends ViewModel<SaveCountryState> {
    public SaveCountryViewModel() {
        super("save country");
        setState(new SaveCountryState());
    }
}
