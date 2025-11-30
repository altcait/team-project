package use_case.ViewSelectedList;

import java.util.ArrayList;
import java.util.List;

public class ViewSelectedListInteractor implements ViewSelectedListInputBoundary {

    private final ViewSelectedListOutputBoundary presenter;

    public ViewSelectedListInteractor(ViewSelectedListOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public ViewSelectedListResponseModel viewSelectedList(ViewSelectedListRequestModel requestModel) {
        String username = requestModel.getUsername();
        String listName = requestModel.getListName();

        String description;
        List<String> countries = new ArrayList<>();

        switch (listName) {
            case "Example List":
                description = "Example description for Example List";
                countries.add("Canada");
                countries.add("Japan");
                countries.add("Germany");
                break;

            case "Asia Trip":
                description = "Countries I'd like to visit on a future Asia trip.";
                countries.add("Japan");
                countries.add("South Korea");
                countries.add("Thailand");
                break;

            case "Europe Bucket List":
                description = "Dream destinations across Europe.";
                countries.add("France");
                countries.add("Italy");
                countries.add("Spain");
                break;

            default:
                description = "No description yet for " + listName + ".";
        }

        ViewSelectedListResponseModel response =
                new ViewSelectedListResponseModel(username, listName, description, countries);

        return presenter.prepareSuccessView(response);
    }
}
