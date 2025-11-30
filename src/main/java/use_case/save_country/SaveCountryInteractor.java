package use_case.save_country;

import use_case.login.LoginUserAccess;

/**
 * The Save Country Interactor.
 */
public class SaveCountryInteractor implements SaveCountryInputBoundary {
    private final SaveCountryDataAccessInterface saveCountryDataAccessObject;
    private final SaveCountryOutputBoundary saveCountryPresenter;
    private final LoginUserAccess loginUserAccess;

    public SaveCountryInteractor(LoginUserAccess loginUserAccess,
                                 SaveCountryDataAccessInterface saveCountryDataAccessInterface,
                                 SaveCountryOutputBoundary saveCountryOutputBoundary) {
        this.loginUserAccess = loginUserAccess;
        this.saveCountryDataAccessObject = saveCountryDataAccessInterface;
        this.saveCountryPresenter = saveCountryOutputBoundary;
    }

    @Override
    public void execute(SaveCountryInputData saveCountryInputData) {
        // Set current user's username here
        String username = loginUserAccess.getCurrentUsername();
        username = (username != null) ? username : "caitlinhen001";

        // If country already exists in the given list for the given user, return error message
        if (saveCountryDataAccessObject.countryExists(username,
                saveCountryInputData.getListName(),
                saveCountryInputData.getCountryCode()
                )) {
            saveCountryPresenter.prepareFailView(saveCountryInputData.getCountryCode() + " already exists in your " + saveCountryInputData.getListName() + " list.");
        } else {
            // Happy Path:
            // Add country & notes to user's favourites list
            saveCountryDataAccessObject.addCountry(username,
                    saveCountryInputData.getListName(),
                    saveCountryInputData.getCountryCode(),
                    saveCountryInputData.getNotes());
            // prepare output data
            final SaveCountryOutputData saveCountryOutputData = new SaveCountryOutputData(saveCountryInputData.getCountryCode(), saveCountryInputData.getListName());
            // tell presenter to prepare success view
            saveCountryPresenter.prepareSuccessView(saveCountryOutputData);
        }
    }
}
