package use_case.save_country;

import data_access.FileUserDataAccessObject;
import data_access.UserCSVDataAccess;
import entity.UserFactory;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SaveCountryInteractorTest {
    private static final String STARTER_JSON = "{"
            + "\"caitlinhen001\":{"
            + "\"visited\":{"
            + "\"description\":\"\","
            + "\"countries\":{"
            + "\"HKG\":\"amazing!\""
            + "}"
            + "}"
            + "}"
            + "}";


    @Test
    public void successTest() throws IOException {
        // test adding a new country to an existing user's existing list
        // Prep classes
        SaveCountryInputData inputData = new SaveCountryInputData("AUS", "visited", "dream vacation");

        Path tempFile = Files.createTempFile("favourites_for_test", ".json");
        SaveCountryDataAccessInterface favouritesRepository = new FileUserDataAccessObject(tempFile.toString());
        UserCSVDataAccess userCSVDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());
        userCSVDataAccess.setCurrentUsername("caitlinhen001");

        // Stub presenter
        SaveCountryOutputBoundary successPresenter = createSuccessPresenter(favouritesRepository);

        SaveCountryInputBoundary interactor = new SaveCountryInteractor(userCSVDataAccess, favouritesRepository, successPresenter);

        // Execute method
        interactor.execute(inputData);
        // Clean up test file
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void failureCountryAlreadyInListTest() throws IOException {
        // test adding a new country to an existing user's existing list
        // Prep classes
        SaveCountryInputData inputData = new SaveCountryInputData("HKG", "visited", "was great!");

        Path tempFile = Files.createTempFile("favourites_for_test", ".json");
        Files.writeString(tempFile, STARTER_JSON);

        SaveCountryDataAccessInterface favouritesRepository = new FileUserDataAccessObject(tempFile.toString());

        UserCSVDataAccess userCSVDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());
        userCSVDataAccess.setCurrentUsername("caitlinhen001");

        // Stub presenter
        SaveCountryOutputBoundary failurePresenter = createFailurePresenter(inputData);

        SaveCountryInputBoundary interactor = new SaveCountryInteractor(userCSVDataAccess, favouritesRepository, failurePresenter);

        // Execute method
        interactor.execute(inputData);

        // clean up test file
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void fetchListNamesTest() throws IOException {
        // Prep classes
        Path tempFile = Files.createTempFile("favourites_for_test", ".json");
        Files.writeString(tempFile, STARTER_JSON);

        SaveCountryDataAccessInterface favouritesRepository = new FileUserDataAccessObject(tempFile.toString());

        UserCSVDataAccess userCSVDataAccess = new UserCSVDataAccess("users.csv", new UserFactory());
        userCSVDataAccess.setCurrentUsername("caitlinhen001");
        SaveCountryOutputBoundary listNamesPresenter = createPrepareListNamesPresenter(favouritesRepository);

        SaveCountryInputBoundary interactor = new SaveCountryInteractor(userCSVDataAccess, favouritesRepository, listNamesPresenter);

        // Execute method
        interactor.fetchListNames();

        // clean up test file
        Files.deleteIfExists(tempFile);
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

            @Override
            public void prepareListNames(List<String> listNames) {
                fail("Reaching this method is unexpected");
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
                String errorMessage = inputData.getCountryCode() + " already exists in your " + inputData.getListName() + " list!";
                assertEquals(errorMessage, err);
            }

            @Override
            public void prepareListNames(List<String> listNames) {
                return;
            }
        };
    }

    private static SaveCountryOutputBoundary createPrepareListNamesPresenter(SaveCountryDataAccessInterface favouritesRepository) {
        return new SaveCountryOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveCountryOutputData saveCountryOutputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String err) {
                fail("Use case failure is unexpected");
            }

            @Override
            public void prepareListNames(List<String> listNames) {
                assertEquals(1, listNames.size());
                assertEquals("visited", listNames.get(0));
            }
        };
    }
}

