package interface_adapter.RetrieveSavedLists;

import use_case.RetrieveSavedLists.ViewSavedListsInputBoundary;
import use_case.RetrieveSavedLists.ViewSavedListsRequestModel;
import use_case.RetrieveSavedLists.ViewSavedListsResponseModel;

public class ViewSavedListsController {

    private final ViewSavedListsInputBoundary interactor;

    public ViewSavedListsController(ViewSavedListsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public ViewSavedListsResponseModel viewLists(String username) {
        ViewSavedListsRequestModel request = new ViewSavedListsRequestModel(username);
        return interactor.viewLists(request);
    }
}
