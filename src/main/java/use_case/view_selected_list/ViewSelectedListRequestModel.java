package use_case.view_selected_list;

public class ViewSelectedListRequestModel {

    private final String username;
    private final String listName;

    public ViewSelectedListRequestModel(String username, String listName) {
        this.username = username;
        this.listName = listName;
    }

    public String getUsername() {
        return username;
    }

    public String getListName() {
        return listName;
    }
}
