package interface_adapter.profile;

public class EditProfileState {
    private String language = "";
    private String bio = "";

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
