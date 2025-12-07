package interface_adapter.retrieve_saved_lists;

import use_case.retrieve_saved_lists.ViewSavedListsInputBoundary;
import use_case.retrieve_saved_lists.ViewSavedListsRequestModel;
import use_case.retrieve_saved_lists.ViewSavedListsResponseModel;

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
