package use_case.retrieve_saved_lists;

public class ViewSavedListsRequestModel {

    private final String username;

    public ViewSavedListsRequestModel(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
