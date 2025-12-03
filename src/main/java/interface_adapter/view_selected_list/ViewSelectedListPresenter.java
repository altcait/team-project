package interface_adapter.view_selected_list;

import use_case.view_selected_list.ViewSelectedListOutputBoundary;
import use_case.view_selected_list.ViewSelectedListResponseModel;

public class ViewSelectedListPresenter implements ViewSelectedListOutputBoundary {

    private final ViewSelectedListViewModel viewModel;

    public ViewSelectedListPresenter(ViewSelectedListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ViewSelectedListResponseModel prepareSuccessView(ViewSelectedListResponseModel response) {
        viewModel.setCurrentUsername(response.getUsername());
        viewModel.setCurrentListName(response.getListName());
        viewModel.setDescription(response.getDescription());
        viewModel.setCountries(response.getCountries());
        viewModel.setErrorMessage(null);
        return response;
    }

    @Override
    public ViewSelectedListResponseModel prepareFailView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
        // Return current state of the view model as a response
        return new ViewSelectedListResponseModel(
                viewModel.getCurrentUsername(),
                viewModel.getCurrentListName(),
                viewModel.getDescription(),
                viewModel.getCountries()
        );
    }
}
