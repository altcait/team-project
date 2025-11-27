package interface_adapter.RetrieveSavedLists;

import use_case.RetrieveSavedLists.ViewSavedListsOutputBoundary;
import use_case.RetrieveSavedLists.ViewSavedListsResponseModel;

public class ViewSavedListsPresenter implements ViewSavedListsOutputBoundary {

    private final ViewSavedListsViewModel viewModel;

    public ViewSavedListsPresenter(ViewSavedListsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ViewSavedListsResponseModel prepareSuccessView(ViewSavedListsResponseModel response) {
        viewModel.setListNames(response.getListNames());
        viewModel.setDescriptions(response.getDescriptions());
        viewModel.setErrorMessage(null);
        return response;
    }

    @Override
    public ViewSavedListsResponseModel prepareFailView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
        viewModel.setListNames(null);
        viewModel.setDescriptions(null);
        return new ViewSavedListsResponseModel(null, null);
    }
}
