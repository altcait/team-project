package use_case.ViewSelectedList;

import java.util.List;

public class ViewSelectedListResponseModel {

    private final String listName;
    private final String description;
    private final List<String> countries;

    public ViewSelectedListResponseModel(String listName,
                                         String description,
                                         List<String> countries) {
        this.listName = listName;
        this.description = description;
        this.countries = countries;
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
