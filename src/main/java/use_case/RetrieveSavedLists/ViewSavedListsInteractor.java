package use_case.RetrieveSavedLists;

import java.util.ArrayList;
import java.util.List;

public class ViewSavedListsInteractor implements ViewSavedListsInputBoundary {

    private final ViewSavedListsOutputBoundary presenter;

    // Later you can add a data access object here (e.g., UserDataAccessInterface) if needed.
    public ViewSavedListsInteractor(ViewSavedListsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public ViewSavedListsResponseModel viewLists(ViewSavedListsRequestModel requestModel) {

        // TODO: replace this dummy data with real lists from the logged-in user.
        List<String> listNames = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();

        // Example dummy entry (you can delete this once real logic is added)
        listNames.add("Example List");
        descriptions.add("This is a placeholder list.");

        ViewSavedListsResponseModel response =
                new ViewSavedListsResponseModel(listNames, descriptions);

        return presenter.prepareSuccessView(response);
    }
}
