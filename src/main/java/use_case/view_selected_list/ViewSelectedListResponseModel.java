package use_case.view_selected_list;

import java.util.List;

public class ViewSelectedListResponseModel {

    private final String username;
    private final String listName;
    private final String description;
    private final List<String> countries;

    public ViewSelectedListResponseModel(String username,
                                         String listName,
                                         String description,
                                         List<String> countries) {
        this.username = username;
        this.listName = listName;
        this.description = description;
        this.countries = countries;
    }

    public String getUsername() {
        return username;
    }

    public String getListName() {
        return listName;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getCountries() {
        return countries;
    }
}
