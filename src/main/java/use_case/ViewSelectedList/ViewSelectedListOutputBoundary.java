package use_case.ViewSelectedList;

public interface ViewSelectedListOutputBoundary {

    ViewSelectedListResponseModel prepareSuccessView(ViewSelectedListResponseModel responseModel);

    ViewSelectedListResponseModel prepareFailView(String error);
}
