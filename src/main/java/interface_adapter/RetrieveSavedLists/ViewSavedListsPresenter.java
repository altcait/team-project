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
        // Save username and list names into the ViewModel
        viewModel.setCurrentUsername(response.getUsername());
        viewModel.setListNames(response.getListNames());
        viewModel.setErrorMessage(null);
        return response;
    }

    @Override
    public ViewSavedListsResponseModel prepareFailView(String errorMessage) {
        viewModel.setListNames(null);
        viewModel.setErrorMessage(errorMessage);
        // Keep whatever username is already in the view model
        return new ViewSavedListsResponseModel(viewModel.getCurrentUsername(), null);
    }
}
