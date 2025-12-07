package use_case.signup;

import data_access.UserCSVDataAccess;
import entity.UserFactory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SignUpInteractorTest {

    private static final String HEADER = "username,password,language,bio";

    @Test
    public void failureEmptyUsernameTest() throws IOException {
        Path tempUsers = Files.createTempFile("users_test", ".csv");
        Files.writeString(tempUsers, HEADER + "\n");

        UserCSVDataAccess userAccess =
                new UserCSVDataAccess(tempUsers.toString(), new UserFactory());

        SignUpInput input = new SignUpInput("", "abc");

        SignUpOutputBoundary presenter = createFailurePresenter("Username cannot be empty.");

        SignUpInputBoundary interactor =
                new SignUpInteractor(userAccess, presenter, new UserFactory());

        interactor.execute(input);

        Files.deleteIfExists(tempUsers);
    }

    @Test
    public void failureEmptyPasswordTest() throws IOException {
        Path tempUsers = Files.createTempFile("users_test", ".csv");
        Files.writeString(tempUsers, HEADER + "\n");

        UserCSVDataAccess userAccess =
                new UserCSVDataAccess(tempUsers.toString(), new UserFactory());

        SignUpInput input = new SignUpInput("mafe", "");

        SignUpOutputBoundary presenter = createFailurePresenter("Password cannot be empty.");

        SignUpInputBoundary interactor =
                new SignUpInteractor(userAccess, presenter, new UserFactory());

        interactor.execute(input);

        Files.deleteIfExists(tempUsers);
    }

    @Test
    public void failureUserExistsTest() throws IOException {
        Path tempUsers = Files.createTempFile("users_test", ".csv");

        Files.writeString(tempUsers,
                HEADER + "\n" +
                        "mafe,1234,English,Bio here\n"
        );

        UserCSVDataAccess userAccess =
                new UserCSVDataAccess(tempUsers.toString(), new UserFactory());

        SignUpInput input = new SignUpInput("mafe", "abcd");

        SignUpOutputBoundary presenter = createFailurePresenter("Username already exists.");

        SignUpInputBoundary interactor =
                new SignUpInteractor(userAccess, presenter, new UserFactory());

        interactor.execute(input);

        Files.deleteIfExists(tempUsers);
    }

    @Test
    public void successTest() throws IOException {
        Path tempUsers = Files.createTempFile("users_test", ".csv");
        Files.writeString(tempUsers, HEADER + "\n");

        UserCSVDataAccess userAccess =
                new UserCSVDataAccess(tempUsers.toString(), new UserFactory());

        SignUpInput input = new SignUpInput("newuser", "mypassword");

        SignUpOutputBoundary presenter = createSuccessPresenter(userAccess);

        SignUpInputBoundary interactor =
                new SignUpInteractor(userAccess, presenter, new UserFactory());

        interactor.execute(input);

        Files.deleteIfExists(tempUsers);
    }

    @Test
    public void switchToLoginViewTest() {
        SignUpOutputBoundary presenter = new SignUpOutputBoundary() {
            boolean called = false;

            @Override
            public void prepareSuccessView(SignUpOutput output) {
                fail("Unexpected success");
            }

            @Override
            public void prepareFailureView(String error) {
                fail("Unexpected failure");
            }

            @Override
            public void switchToLoginView() {
                called = true;
            }
        };

        SignUpInputBoundary interactor =
                new SignUpInteractor(null, presenter, new UserFactory());

        interactor.switchToLoginView();
    }


    private static SignUpOutputBoundary createFailurePresenter(String expectedError) {
        return new SignUpOutputBoundary() {
            @Override
            public void prepareSuccessView(SignUpOutput output) {
                fail("Unexpected success");
            }

            @Override
            public void prepareFailureView(String error) {
                assertEquals(expectedError, error);
            }

            @Override
            public void switchToLoginView() { }
        };
    }

    private static SignUpOutputBoundary createSuccessPresenter(UserCSVDataAccess userAccess) {
        return new SignUpOutputBoundary() {
            @Override
            public void prepareSuccessView(SignUpOutput output) {
                assertEquals("newuser", output.getUsername());
                assertTrue(userAccess.existsByName("newuser"));
            }

            @Override
            public void prepareFailureView(String error) {
                fail("Unexpected failure: " + error);
            }

            @Override
            public void switchToLoginView() { }
        };
    }
}
