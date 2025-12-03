package use_case.profile;

import data_access.UserCSVDataAccess;
import entity.UserFactory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileInteractorTest {

    private static final String HEADER = "username,password,language,bio";

    @Test
    public void goToFavoritesTest() {
        ProfileOutputBoundary presenter = new ProfileOutputBoundary() {
            boolean called = false;

            @Override
            public void switchToFavoritesView() {
                called = true;
            }

            @Override public void switchToLoginView() { }
            @Override public void switchToEditProfileView() { }
            @Override public void switchToProfileView() { }
            @Override public void updateProfile(String language, String bio) { }
        };

        ProfileInputBoundary interactor =
                new ProfileInteractor(presenter, null);

        interactor.goToFavorites();
    }

    @Test
    public void logoutTest() {
        ProfileOutputBoundary presenter = new ProfileOutputBoundary() {
            boolean called = false;

            @Override public void switchToFavoritesView() { }
            @Override public void switchToLoginView() { called = true; }
            @Override public void switchToEditProfileView() { }
            @Override public void switchToProfileView() { }
            @Override public void updateProfile(String language, String bio) { }
        };

        ProfileInputBoundary interactor =
                new ProfileInteractor(presenter, null);

        interactor.logout();
    }

    @Test
    public void editProfileTest() {
        ProfileOutputBoundary presenter = new ProfileOutputBoundary() {
            boolean called = false;

            @Override public void switchToFavoritesView() { }
            @Override public void switchToLoginView() { }
            @Override public void switchToEditProfileView() { called = true; }
            @Override public void switchToProfileView() { }
            @Override public void updateProfile(String language, String bio) { }
        };

        ProfileInputBoundary interactor =
                new ProfileInteractor(presenter, null);

        interactor.editProfile();
    }

    @Test
    public void backToProfileTest() {
        ProfileOutputBoundary presenter = new ProfileOutputBoundary() {
            boolean called = false;

            @Override public void switchToFavoritesView() { }
            @Override public void switchToLoginView() { }
            @Override public void switchToEditProfileView() { }
            @Override public void switchToProfileView() { called = true; }
            @Override public void updateProfile(String language, String bio) { }
        };

        ProfileInputBoundary interactor =
                new ProfileInteractor(presenter, null);

        interactor.backToProfile();
    }

    @Test
    public void updateProfileTest() throws IOException {

        Path tempUsers = Files.createTempFile("users_test", ".csv");

        Files.writeString(tempUsers,
                HEADER + "\n" +
                        "mafe,1234,English,Old bio\n"
        );

        UserCSVDataAccess userAccess =
                new UserCSVDataAccess(tempUsers.toString(), new UserFactory());

        userAccess.setCurrentUsername("mafe");

        ProfileOutputBoundary presenter = new ProfileOutputBoundary() {

            @Override public void switchToFavoritesView() { }
            @Override public void switchToLoginView() { }
            @Override public void switchToEditProfileView() { }

            @Override
            public void switchToProfileView() {
                assertEquals("Spanish", userAccess.get("mafe").getLanguage());
                assertEquals("New bio", userAccess.get("mafe").getBio());
            }

            @Override
            public void updateProfile(String language, String bio) {
                assertEquals("Spanish", language);
                assertEquals("New bio", bio);
            }
        };

        ProfileInputBoundary interactor =
                new ProfileInteractor(presenter, userAccess);

        interactor.updateProfile("Spanish", "New bio");

        Files.deleteIfExists(tempUsers);
    }
}
