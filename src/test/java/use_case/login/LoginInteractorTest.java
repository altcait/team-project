package use_case.login;

import data_access.UserCSVDataAccess;
import entity.UserFactory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class LoginInteractorTest {

    private static final String HEADER = "username,password,language,bio";

    @Test
    public void successTest() throws IOException {
        Path tempUsers = Files.createTempFile("users_test", ".csv");

        Files.writeString(tempUsers,
                HEADER + "\n" +
                        "mafe,1234,English,Hello there!\n"
        );

        UserCSVDataAccess userAccess =
                new UserCSVDataAccess(tempUsers.toString(), new UserFactory());

        LoginInput input = new LoginInput("mafe", "1234");

        LoginOutputBoundary presenter = createSuccessPresenter();

        LoginInputBoundary interactor = new LoginInteractor(userAccess, presenter);

        interactor.execute(input);

        Files.deleteIfExists(tempUsers);
    }

    @Test
    public void failureUserDoesNotExistTest() throws IOException {
        Path tempUsers = Files.createTempFile("users_test", ".csv");

        Files.writeString(tempUsers, HEADER + "\n");

        UserCSVDataAccess userAccess =
                new UserCSVDataAccess(tempUsers.toString(), new UserFactory());

        LoginInput input = new LoginInput("ghost", "aaa");

        String expected = "ghost: Account does not exist";

        LoginOutputBoundary presenter = createFailurePresenter(expected);

        LoginInputBoundary interactor = new LoginInteractor(userAccess, presenter);

        interactor.execute(input);

        Files.deleteIfExists(tempUsers);
    }

    @Test
    public void failureIncorrectPasswordTest() throws IOException {
        Path tempUsers = Files.createTempFile("users_test", ".csv");

        Files.writeString(tempUsers,
                HEADER + "\n" +
                        "sam,9999,English,Bio here\n"
        );

        UserCSVDataAccess userAccess =
                new UserCSVDataAccess(tempUsers.toString(), new UserFactory());

        LoginInput input = new LoginInput("sam", "wrong");

        String expected =
                "sam: Incorrect password for \" sam\".";

        LoginOutputBoundary presenter = createFailurePresenter(expected);

        LoginInputBoundary interactor =
                new LoginInteractor(userAccess, presenter);

        interactor.execute(input);

        Files.deleteIfExists(tempUsers);
    }

    private static LoginOutputBoundary createSuccessPresenter() {
        return new LoginOutputBoundary() {
            @Override
            public void prepareSuccess(LoginOutput loginOutput) {
                assertEquals("mafe", loginOutput.getUsername());
            }

            @Override
            public void prepareFailure(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };
    }

    private static LoginOutputBoundary createFailurePresenter(String expectedError) {
        return new LoginOutputBoundary() {
            @Override
            public void prepareSuccess(LoginOutput loginOutput) {
                fail("Unexpected success");
            }

            @Override
            public void prepareFailure(String errorMessage) {
                assertEquals(expectedError, errorMessage);
            }
        };
    }
}

