package use_case.save_country;

import data_access.FileUserDataAccessObject;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SaveCountryInteractorTest {
    @Test
    public void successTest(){
        // test adding a new country to an existing user's existing list
        SaveCountryInputData inputData = new SaveCountryInputData("AUS", "visited", "dream vacation");
        // Create in-memory DAO for testing purposes
        SaveCountryDataAccessInterface favouritesRepository = new FileUserDataAccessObject("src/test/java/use_case/save_country/favourites_for_test.json");

        // Stub presenter
        SaveCountryOutputBoundary successPresenter = createSuccessPresenter(favouritesRepository);

        // create an interactor and execute it
        SaveCountryInputBoundary interactor = new SaveCountryInteractor(favouritesRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureCountryAlreadyInListTest(){
        // test adding a new country to an existing user's existing list
        SaveCountryInputData inputData = new SaveCountryInputData("HKG", "visited", "was great!");
        // Create in-memory DAO for testing purposes
        SaveCountryDataAccessInterface favouritesRepository = new FileUserDataAccessObject("src/test/java/use_case/save_country/favourites_for_test.json");

        // Stub presenter
        SaveCountryOutputBoundary failurePresenter = createFailurePresenter(inputData);

        // create an interactor and execute it
        SaveCountryInputBoundary interactor = new SaveCountryInteractor(favouritesRepository, failurePresenter);
        interactor.execute(inputData);
    }

    private static SaveCountryOutputBoundary createSuccessPresenter(SaveCountryDataAccessInterface favouritesRepository) {
        return new SaveCountryOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveCountryOutputData saveCountryOutputData) {
                // In success view, ensure that Output Data includes the country code and list name that we expect
                assertEquals("AUS", saveCountryOutputData.getCountryCode());
                assertEquals("visited", saveCountryOutputData.getListName());
                // Assert that data access object methods return what we expect
                assertTrue(favouritesRepository.userExists("caitlinhen001"));
                assertTrue(favouritesRepository.listExists("caitlinhen001", "visited"));
                assertTrue(favouritesRepository.countryExists("caitlinhen001", "visited", "AUS"));
            }

            @Override
            public void prepareFailView(String err) {
                fail("Use case failure is unexpected");
            }
        };
    }

    private static SaveCountryOutputBoundary createFailurePresenter(SaveCountryInputData inputData) {
        return new SaveCountryOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveCountryOutputData saveCountryOutputData) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String err) {
                String errorMessage = inputData.getCountryCode() + " already exists in your " + inputData.getListName() + " list.";
                assertEquals(errorMessage, err);
            }
        };
    }
}
