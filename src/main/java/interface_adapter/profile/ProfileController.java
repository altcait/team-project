package interface_adapter.profile;

import use_case.profile.ProfileInputBoundary;

public class ProfileController {

    private final ProfileInputBoundary interactor;

    public ProfileController(ProfileInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void onFavorites() {
        interactor.goToFavorites();
    }

    public void onLogout() {
        interactor.logout();
    }

    public void onEditProfile() {
        interactor.editProfile();
    }

    public void onBackToProfile() {
        interactor.backToProfile();
    }

    public void onSaveProfile(String language, String bio) {
        interactor.updateProfile(language, bio);
    }

    public void onSearches() { interactor.goToSearches(); }
}
