package use_case.RetrieveSavedLists;

public interface ViewSavedListsOutputBoundary {

    ViewSavedListsResponseModel prepareSuccessView(ViewSavedListsResponseModel responseModel);

    ViewSavedListsResponseModel prepareFailView(String error);
}
