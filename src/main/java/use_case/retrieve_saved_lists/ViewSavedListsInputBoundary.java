package use_case.retrieve_saved_lists;

public interface ViewSavedListsInputBoundary {

    /**
     * Load all saved lists for the given user.
     */
    ViewSavedListsResponseModel viewLists(ViewSavedListsRequestModel requestModel);
}
