package use_case.profile;

public class ProfileInteractor implements ProfileInputBoundary {
    private final ProfileOutputBoundary presenter;

    public ProfileInteractor(ProfileOutputBoundary presenter) {
        this.presenter = presenter;
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
        presenter.updateProfile(language, bio);
    }
}
