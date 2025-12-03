package interface_adapter.save_country;

import use_case.save_country.SaveCountryInputBoundary;
import use_case.save_country.SaveCountryInputData;

/**
 * Controller for the Save Country Use Case.
 */
public class SaveCountryController {
    private final SaveCountryInputBoundary saveCountryUseCaseInteractor;

    public SaveCountryController(SaveCountryInputBoundary saveCountryUseCaseInteractor) {
        this.saveCountryUseCaseInteractor = saveCountryUseCaseInteractor;
    }

    public void fetchListNames() {
        saveCountryUseCaseInteractor.fetchListNames();
    }


    /**
     * Executes the Save Country Use Case.
     * @param countryCode the country to add to the favourites list
     * @param listName the name of the list to add the country to
     * @param notes the notes to be associated with the country
     */
    public void execute(String countryCode, String listName, String notes) {
        final SaveCountryInputData saveCountryInputData = new SaveCountryInputData(countryCode, listName, notes);
        saveCountryUseCaseInteractor.execute(saveCountryInputData);
    }
}
