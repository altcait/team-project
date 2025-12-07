package interface_adapter.view_selected_list;

import use_case.view_selected_list.ViewSelectedListInputBoundary;
import use_case.view_selected_list.ViewSelectedListRequestModel;
import use_case.view_selected_list.ViewSelectedListResponseModel;

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
