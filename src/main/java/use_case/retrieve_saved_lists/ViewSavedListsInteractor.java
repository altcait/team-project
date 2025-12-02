package use_case.retrieve_saved_lists;

import data_access.FavoritesReadDataAccess;
import java.util.List;

public class ViewSavedListsInteractor implements ViewSavedListsInputBoundary {

    private final ViewSavedListsOutputBoundary presenter;
    private final FavoritesReadDataAccess dataAccess;

    public ViewSavedListsInteractor(ViewSavedListsOutputBoundary presenter,
                                    FavoritesReadDataAccess dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public ViewSavedListsResponseModel viewLists(ViewSavedListsRequestModel requestModel) {
        String username = requestModel.getUsername();

        // Get actual list names from JSON file via interface
        List<String> listNames = dataAccess.getUserLists(username);

        if (listNames.isEmpty()) {
            return presenter.prepareFailView("No lists found for user: " + username);
        }

        ViewSavedListsResponseModel response =
                new ViewSavedListsResponseModel(username, listNames);

        return presenter.prepareSuccessView(response);
    }
}