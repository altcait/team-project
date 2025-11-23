package use_case.save_country;

/**
 * The Save Country Interactor.
 */
public class SaveCountryInteractor implements SaveCountryInputBoundary {
    private final SaveCountryDataAccessInterface saveCountryDataAccessObject;
    private final SaveCountryOutputBoundary saveCountryPresenter;

    public SaveCountryInteractor(SaveCountryDataAccessInterface saveCountryDataAccessInterface,
                                 SaveCountryOutputBoundary saveCountryOutputBoundary) {
        this.saveCountryDataAccessObject = saveCountryDataAccessInterface;
        this.saveCountryPresenter = saveCountryOutputBoundary;
    }

    @Override
    public void execute(SaveCountryInputData saveCountryInputData) {
        // If country already exists in the given list for the given user, return error message
        if (saveCountryDataAccessObject.countryExists("caitlinhen001",
                saveCountryInputData.getListName(),
                saveCountryInputData.getCountryCode()
                )) {
            saveCountryPresenter.prepareFailView(saveCountryInputData.getCountryCode() + " already exists in your " + saveCountryInputData.getListName() + " list.");
        } else {
            // Happy Path:
            // Add country & notes to user's favourites list
            saveCountryDataAccessObject.addCountry("caitlinhen001",
                    saveCountryInputData.getListName(),
                    saveCountryInputData.getCountryCode(),
                    saveCountryInputData.getNotes());
            // prepare output data
            final SaveCountryOutputData saveCountryOutputData = new SaveCountryOutputData("HKG", "visited");
            // tell presenter to prepare success view
            saveCountryPresenter.prepareSuccessView(saveCountryOutputData);
        }
    }
}
