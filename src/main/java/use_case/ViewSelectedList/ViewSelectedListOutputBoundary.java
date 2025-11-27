package use_case.ViewSelectedList;

public interface ViewSelectedListOutputBoundary {

    ViewSelectedListResponseModel prepareSuccessView(ViewSelectedListResponseModel response);

    ViewSelectedListResponseModel prepareFailView(String errorMessage);
}
