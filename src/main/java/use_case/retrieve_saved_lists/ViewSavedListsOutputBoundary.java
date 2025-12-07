package use_case.retrieve_saved_lists;

public interface ViewSavedListsOutputBoundary {

    ViewSavedListsResponseModel prepareSuccessView(ViewSavedListsResponseModel responseModel);

    ViewSavedListsResponseModel prepareFailView(String error);
}
