package use_case.profile;

import entity.User;
import data_access.*;

public class ProfileInteractor implements ProfileInputBoundary {
    private final ProfileOutputBoundary presenter;
    private final UserCSVDataAccess userAccess;

    public ProfileInteractor(ProfileOutputBoundary presenter, UserCSVDataAccess userDataAccess) {
        this.presenter = presenter;
        this.userAccess = userDataAccess;
    }

    @Override
    public void goToFavorites() {
        presenter.switchToFavoritesView();
    }

    @Override
    public void logout() {
        presenter.switchToLoginView();
    }

    @Override
    public void editProfile() {
        presenter.switchToEditProfileView();
    }

    @Override
    public void backToProfile() {
        presenter.switchToProfileView();
    }

    @Override
    public void updateProfile(String language, String bio) {

        String username = userAccess.getCurrentUsername();

        User user = userAccess.get(username);

        user.setLanguage(language);
        user.setBio(bio);
        userAccess.save(user);

        userAccess.save(user);

        presenter.updateProfile(language, bio);

        presenter.switchToProfileView();

    }
}
