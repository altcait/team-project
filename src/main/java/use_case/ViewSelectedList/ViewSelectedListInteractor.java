package use_case.ViewSelectedList;

import java.util.ArrayList;
import java.util.List;

public class ViewSelectedListInteractor implements ViewSelectedListInputBoundary {

    private final ViewSelectedListOutputBoundary presenter;

    // Later you can add a data access object here (to read from JSON).
    public ViewSelectedListInteractor(ViewSelectedListOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public ViewSelectedListResponseModel viewSelectedList(ViewSelectedListRequestModel requestModel) {

        String username = requestModel.getUsername();
        String listName = requestModel.getListName();

        // TODO: replace this dummy data with real data from JSON for (username, listName).

        String description = "Example description for " + listName;

        List<String> countries = new ArrayList<>();
        countries.add("Canada");
        countries.add("Japan");
        countries.add("Germany");

        ViewSelectedListResponseModel response =
                new ViewSelectedListResponseModel(listName, description, countries);

        return presenter.prepareSuccessView(response);
    }
}
