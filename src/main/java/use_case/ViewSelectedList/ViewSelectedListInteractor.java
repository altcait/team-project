package use_case.ViewSelectedList;

import use_case.FavoritesList.FavoritesReadDataAccess;
import java.util.List;

public class ViewSelectedListInteractor implements ViewSelectedListInputBoundary {

    private final ViewSelectedListOutputBoundary presenter;
    private final FavoritesReadDataAccess dataAccess;

    public ViewSelectedListInteractor(ViewSelectedListOutputBoundary presenter,
                                      FavoritesReadDataAccess dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public ViewSelectedListResponseModel viewSelectedList(ViewSelectedListRequestModel requestModel) {
        String username = requestModel.getUsername();
        String listName = requestModel.getListName();

        // Get actual data from JSON file via interface
        String description = dataAccess.getListDescription(username, listName);
        List<String> countries = dataAccess.getCountriesInList(username, listName);

        ViewSelectedListResponseModel response =
                new ViewSelectedListResponseModel(username, listName, description, countries);

        return presenter.prepareSuccessView(response);
    }
}