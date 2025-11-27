package use_case.RetrieveSavedLists;

public interface ViewSavedListsOutputBoundary {

    ViewSavedListsResponseModel prepareSuccessView(ViewSavedListsResponseModel response);

    ViewSavedListsResponseModel prepareFailView(String errorMessage);
}
