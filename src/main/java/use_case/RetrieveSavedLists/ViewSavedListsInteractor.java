package use_case.RetrieveSavedLists;

import java.util.ArrayList;
import java.util.List;

public class ViewSavedListsInteractor implements ViewSavedListsInputBoundary {

    private final ViewSavedListsOutputBoundary presenter;

    public ViewSavedListsInteractor(ViewSavedListsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public ViewSavedListsResponseModel viewLists(ViewSavedListsRequestModel requestModel) {
        String username = requestModel.getUsername();

        // DUMMY DATA: pretend this user has 3 lists
        List<String> listNames = new ArrayList<>();
        listNames.add("Example List");
        listNames.add("Asia Trip");
        listNames.add("Europe Bucket List");

        ViewSavedListsResponseModel response =
                new ViewSavedListsResponseModel(username, listNames);

        return presenter.prepareSuccessView(response);
    }
}
