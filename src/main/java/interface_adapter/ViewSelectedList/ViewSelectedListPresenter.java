package interface_adapter.ViewSelectedList;

import use_case.ViewSelectedList.ViewSelectedListOutputBoundary;
import use_case.ViewSelectedList.ViewSelectedListResponseModel;

public class ViewSelectedListPresenter implements ViewSelectedListOutputBoundary {

    private final ViewSelectedListViewModel viewModel;

    public ViewSelectedListPresenter(ViewSelectedListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ViewSelectedListResponseModel prepareSuccessView(ViewSelectedListResponseModel response) {
        viewModel.setCurrentListName(response.getListName());
        viewModel.setDescription(response.getDescription());
        viewModel.setCountries(response.getCountries());
        viewModel.setErrorMessage(null);
        return response;
    }

    @Override
    public ViewSelectedListResponseModel prepareFailView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
        viewModel.setDescription(null);
        viewModel.setCountries(null);
        return new ViewSelectedListResponseModel(null, null, null);
    }
}
