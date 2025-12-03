package use_case.retrieve_saved_lists;

import java.util.List;

public class ViewSavedListsResponseModel {

    private final String username;
    private final List<String> listNames;

    public ViewSavedListsResponseModel(String username, List<String> listNames) {
        this.username = username;
        this.listNames = listNames;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getListNames() {
        return listNames;
    }
}
