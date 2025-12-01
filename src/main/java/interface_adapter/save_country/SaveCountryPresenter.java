package interface_adapter.save_country;

import use_case.save_country.SaveCountryOutputBoundary;
import use_case.save_country.SaveCountryOutputData;

import java.util.List;

/**
 * The Presenter for the Save Country Use Case.
 */
public class SaveCountryPresenter implements SaveCountryOutputBoundary {
    private final SaveCountryViewModel saveCountryViewModel;

    public SaveCountryPresenter(SaveCountryViewModel saveCountryViewModel) {
        this.saveCountryViewModel = saveCountryViewModel;
    }

    @Override
    public void prepareListNames(List<String> listNames) {
        SaveCountryState saveCountryState = saveCountryViewModel.getState();
        // set list names
        saveCountryState.setLists(listNames);
    }

    /**
     * Prepares the success view for the Save Country Use Case.
     *
     * @param saveCountryOutputData the output data
     */
    @Override
    public void prepareSuccessView(SaveCountryOutputData saveCountryOutputData) {
        SaveCountryState saveCountryState = saveCountryViewModel.getState();

        // Build result string with Output Data
        String countryCode = saveCountryOutputData.getCountryCode();
        String listName = saveCountryOutputData.getListName();
        saveCountryState.setResultString(countryCode + " successfully added to your " + listName + " list!");

        // reset fields
        saveCountryState.setCountryCode("");
        saveCountryState.setListName("");
        saveCountryState.setNotes("");

        saveCountryViewModel.firePropertyChange(); // tell the view model to notify the view of any updates
    }

    /**
     * Prepares the failure view for the Save Country Use Case.
     *
     * @param errorMessage the explanation of the failure
     */
    @Override
    public void prepareFailView(String errorMessage) {
        SaveCountryState saveCountryState = saveCountryViewModel.getState();
        saveCountryState.setResultString(errorMessage);

        saveCountryViewModel.firePropertyChange(); // tell the view model to notify the view of any updates
    }
}
