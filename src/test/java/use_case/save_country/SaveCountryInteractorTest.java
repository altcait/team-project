package use_case.save_country;

import data_access.FileUserDataAccessObject;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SaveCountryInteractorTest {
    @Test
    public void successTest(){
        SaveCountryInputData inputData = new SaveCountryInputData("HKG", "visited", "dream vacation");
        // Create in-memory DAO for testing purposes
        SaveCountryDataAccessInterface favouritesRepository = new FileUserDataAccessObject("src/test/java/use_case/save_country/favourites_for_test.json");

        // Stub presenter
        SaveCountryOutputBoundary successPresenter = createTestPresenter(favouritesRepository);

        // create an interactor and execute it
        SaveCountryInputBoundary interactor = new SaveCountryInteractor(favouritesRepository, successPresenter);
        interactor.execute(inputData);
    }

    private static SaveCountryOutputBoundary createTestPresenter(SaveCountryDataAccessInterface favouritesRepository) {
        return new SaveCountryOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveCountryOutputData saveCountryOutputData) {
                // In success view, ensure that Output Data includes the country code and list name that we expect
                assertEquals("HKG", saveCountryOutputData.getCountryCode());
                assertEquals("visited", saveCountryOutputData.getListName());
                // Assert that data access object methods return what we expect
                assertTrue(favouritesRepository.userExists("caitlinhen001"));
                assertTrue(favouritesRepository.listExists("caitlinhen001", "visited"));
                assertTrue(favouritesRepository.countryExists("caitlinhen001", "visited", "HKG"));

            }

            @Override
            public void prepareFailView(String err) {
                fail("Use case failure is unexpected");
            }
        };
    }
}
