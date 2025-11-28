package use_case.RetrieveSavedLists;

public interface ViewSavedListsInputBoundary {

    /**
     * Load all saved lists for the given user.
     */
    ViewSavedListsResponseModel viewLists(ViewSavedListsRequestModel requestModel);
}
