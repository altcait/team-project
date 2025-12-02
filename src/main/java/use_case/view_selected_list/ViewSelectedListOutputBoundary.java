package use_case.view_selected_list;

public interface ViewSelectedListOutputBoundary {

    ViewSelectedListResponseModel prepareSuccessView(ViewSelectedListResponseModel responseModel);

    ViewSelectedListResponseModel prepareFailView(String error);
}
