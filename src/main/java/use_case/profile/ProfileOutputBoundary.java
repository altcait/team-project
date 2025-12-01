package use_case.profile;

public interface ProfileOutputBoundary {
    void switchToFavoritesView();
    void switchToLoginView();
    void switchToEditProfileView();
    void switchToProfileView();
    void updateProfile(String language, String bio);
    void goToSearches();
}
