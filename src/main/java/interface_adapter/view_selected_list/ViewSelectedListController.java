package interface_adapter.view_selected_list;

import use_case.ViewSelectedList.ViewSelectedListInputBoundary;
import use_case.ViewSelectedList.ViewSelectedListRequestModel;
import use_case.ViewSelectedList.ViewSelectedListResponseModel;

public class ViewSelectedListController {

    private final ViewSelectedListInputBoundary interactor;

    public ViewSelectedListController(ViewSelectedListInputBoundary interactor) {
        this.interactor = interactor;
    }

    public ViewSelectedListResponseModel viewSelectedList(String username, String listName) {
        ViewSelectedListRequestModel request =
                new ViewSelectedListRequestModel(username, listName);
        return interactor.viewSelectedList(request);
    }
}
