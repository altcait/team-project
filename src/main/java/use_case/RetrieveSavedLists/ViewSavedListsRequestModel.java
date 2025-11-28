package use_case.RetrieveSavedLists;

public class ViewSavedListsRequestModel {

    private final String username;

    public ViewSavedListsRequestModel(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
